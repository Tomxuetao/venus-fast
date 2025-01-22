package com.venus.common.utils;

import cn.hutool.core.util.ZipUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.venus.common.exception.VenusException;
import com.venus.modules.geo.dto.GeoDataDTO;
import com.venus.modules.geo.entity.GeoDataEntity;
import org.apache.commons.io.FileUtils;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.simplify.TopologyPreservingSimplifier;
import org.opengis.feature.simple.SimpleFeature;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * GeoDataUtils
 */
public class GeoDataUtils {
    private static final Logger logger = LoggerFactory.getLogger(GeoDataUtils.class);

    private static final Set<String> REQUIRED_FILES = new HashSet<>(Arrays.asList(".shp", ".dbf", ".shx", ".prj"));

    private static final CoordinateReferenceSystem WGS84;

    static {
        try {
            WGS84 = CRS.decode("EPSG:4326", true);
        } catch (FactoryException e) {
            throw new VenusException("定义坐标系失败", e);
        }
    }

    /**
     * 检查文件名列表是否包含所需的文件
     *
     * @param fileNames 文件名列表
     * @return boolean
     */
    public static boolean containsRequiredFiles(List<String> fileNames) {
        return containsRequiredFiles(fileNames, REQUIRED_FILES);
    }

    /**
     * 检查文件名列表是否包含所需的文件
     *
     * @param fileNames 文件名列表
     * @param extNames  扩展名集合
     * @return boolean
     */
    public static boolean containsRequiredFiles(List<String> fileNames, Set<String> extNames) {
        // 获取文件名中的扩展名集合
        Set<String> fileExtensions = fileNames.stream()
                .map(name -> name.substring(name.lastIndexOf('.')).toLowerCase())
                .collect(Collectors.toSet());
        return fileExtensions.containsAll(extNames);
    }

    public static List<GeoDataEntity> createFromFeatures(DefaultFeatureCollection featureCollection) {
        List<GeoDataEntity> dataList = new ArrayList<>();
        featureCollection.forEach(feature -> {
            GeoDataEntity geoDataEntity = new GeoDataEntity();
            geoDataEntity.setGeom((Geometry) feature.getDefaultGeometry());
            geoDataEntity.setProps(feature.getAttribute("__JSON_DATA__").toString());
            dataList.add(geoDataEntity);
        });
        return dataList;
    }

    public static List<GeoDataEntity> createFromShpDir(File dir) {
        return createFromFeatures(readShp(dir));
    }

    /**
     * 读取 Shp 文件
     *
     * @param dir 文件夹
     */
    public static DefaultFeatureCollection readShp(File dir) {
        return readShp(dir, WGS84);
    }

    /**
     * 读取 Shp 文件
     *
     * @param dir       文件夹
     * @param targetCRS 目标坐标系
     */
    public static DefaultFeatureCollection readShp(File dir, CoordinateReferenceSystem targetCRS) {
        DefaultFeatureCollection newFeatureCollection = new DefaultFeatureCollection();
        try {
            File[] files = dir.listFiles();
            if(files == null || files.length == 0) {
                return newFeatureCollection;
            }
            Stream<File> fileStream = Arrays.stream(files).filter(file -> file.getName().endsWith("shp"));
            File inputShpFile = fileStream.findFirst().orElse(null);

            if(inputShpFile != null) {
                FileDataStore dataStore = FileDataStoreFinder.getDataStore(inputShpFile);
                SimpleFeatureSource featureSource = dataStore.getFeatureSource();
                SimpleFeatureType featureType = featureSource.getSchema();
                CoordinateReferenceSystem sourceCRS = featureType.getCoordinateReferenceSystem();

                boolean isSameCRS = CRS.equalsIgnoreMetadata(sourceCRS, targetCRS);
                MathTransform transform = null;
                if(!isSameCRS) {
                    transform = CRS.findMathTransform(sourceCRS, targetCRS, true);
                }

                try (SimpleFeatureIterator iterator = featureSource.getFeatures().features()) {
                    while (iterator.hasNext()) {
                        SimpleFeature feature = iterator.next();
                        // 获取几何数据和属性
                        Geometry geometry = (Geometry) feature.getDefaultGeometry();
                        Geometry transformedGeometry = transform == null ? geometry : JTS.transform(geometry, transform);

                        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
                        // 复制原始类型
                        typeBuilder.init(featureType);
                        // 添加新的字段，类型为 Map
                        typeBuilder.add("__JSON_DATA__", Object.class);
                        SimpleFeatureType newFeatureType = typeBuilder.buildFeatureType();

                        // 创建新的 Feature
                        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(newFeatureType);
                        featureBuilder.addAll(feature.getAttributes());
                        featureBuilder.set("the_geom", transformedGeometry);
                        SimpleFeature newFeature = featureBuilder.buildFeature(null);
                        Map<String, Object> featureData = new HashMap<>();
                        List<String> excludeFields = Arrays.asList("the_geom", "__JSON_DATA__");
                        newFeature.getProperties().forEach(prop -> {
                            if(!excludeFields.contains(prop.getName().toString())) {
                                featureData.put(prop.getName().toString(), prop.getValue().toString());
                            }
                        });
                        Gson gson = new Gson();
                        newFeature.setAttribute("__JSON_DATA__", gson.toJson(featureData));
                        newFeatureCollection.add(newFeature);
                    }
                }
                dataStore.dispose();
            }
        } catch (Exception e) {
            logger.error("read shp error", e);
            throw new VenusException("读取Shp文件失败", e);
        }
        return newFeatureCollection;
    }

    /**
     * 创建 Shapefile 的 Schema
     */
    private static SimpleFeatureTypeBuilder createShpSchema() {
        // 定义 Shapefile 的 Schema（包括几何和部分属性字段）
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.setName("Features");
        typeBuilder.add("the_geom", MultiPolygon.class);
        typeBuilder.add("id", Long.class);

        // 设置坐标系（默认 WGS 84）
        typeBuilder.setCRS(WGS84);

        return typeBuilder;
    }

    /**
     * 创建属性字段
     *
     * @param typeBuilder 类型构造器
     * @param geoDataDTO  数据对象
     */
    private static void createPropsByGeoData(SimpleFeatureTypeBuilder typeBuilder, GeoDataDTO geoDataDTO) {
        Type mapType = new TypeToken<Map<String, Object>>() {
        }.getType();
        Gson gson = new Gson();
        Map<String, Object> props = gson.fromJson(geoDataDTO.getProps(), mapType);
        // 添加属性字段
        for (Map.Entry<String, Object> entry : props.entrySet()) {
            typeBuilder.add(entry.getKey(), String.class);
        }
    }

    /**
     * 创建 FeatureCollection
     *
     * @param geoDataList 数据列表
     * @param featureType 类型
     */
    private static DefaultFeatureCollection createFeatureCollection(List<GeoDataDTO> geoDataList, SimpleFeatureType featureType) {
        DefaultFeatureCollection featureCollection = new DefaultFeatureCollection(null, featureType);
        GeometryJSON geometryJSON = new GeometryJSON();

        Gson gson = new Gson();
        for (GeoDataDTO entity : geoDataList) {
            // 解析几何数据
            Geometry geometry = null;
            try {
                geometry = geometryJSON.read(entity.getGeom());
            } catch (IOException e) {
                throw new VenusException("读取Geometry失败", e);
            }

            // 创建 Feature
            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
            featureBuilder.add(geometry);
            featureBuilder.add(entity.getId());
            Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> props = gson.fromJson(entity.getProps(), mapType);
            for (Map.Entry<String, Object> entry : props.entrySet()) {
                featureBuilder.add(entry.getValue().toString());
            }

            SimpleFeature feature = featureBuilder.buildFeature(null);
            featureCollection.add(feature);
        }
        return featureCollection;
    }

    /**
     * 导出为 Shapefile
     *
     * @param geoDataList 数据列表
     * @param response    响应对象
     */
    public static void toShp(List<GeoDataDTO> geoDataList, HttpServletResponse response) {
        File tempDir = null;
        try {
            tempDir = Files.createTempDirectory("shp-zip-data").toFile();
            String shpPath = tempDir.getAbsolutePath() + File.separator + "data.shp";

            File outputShpFile = new File(shpPath);

            // 2. 定义 Shapefile 的 Schema（包括几何和属性字段）
            SimpleFeatureTypeBuilder typeBuilder = createShpSchema();

            // 添加属性字段
            if(!geoDataList.isEmpty()) {
                createPropsByGeoData(typeBuilder, geoDataList.get(0));
            }
            SimpleFeatureType featureType = typeBuilder.buildFeatureType();

            // 3. 创建 Feature 集合
            DefaultFeatureCollection featureCollection = createFeatureCollection(geoDataList, featureType);

            // 4. 创建 Shapefile 数据存储
            Map<String, Object> params = new HashMap<>();
            params.put("url", outputShpFile.toURI().toURL());
            ShapefileDataStoreFactory factory = new ShapefileDataStoreFactory();
            ShapefileDataStore newDataStore = (ShapefileDataStore) factory.createNewDataStore(params);
            newDataStore.setCharset(StandardCharsets.UTF_8);

            // 5. 写入 Schema 和数据
            newDataStore.createSchema(featureType);
            SimpleFeatureStore featureStore = (SimpleFeatureStore) newDataStore.getFeatureSource(newDataStore.getTypeNames()[0]);
            featureStore.addFeatures(featureCollection);

            // 6. 写入投影文件（.prj）
            writeProjFile(outputShpFile, WGS84);

            // 7. 释放资源
            newDataStore.dispose();

            File zipFile = ZipUtil.zip(tempDir);

            writeZipStream(zipFile, response);
        } catch (Exception e) {
            logger.error("create temp dir error", e);
            throw new VenusException("导出为 Shapefile 失败", e);
        } finally {
            if(tempDir != null) {
                try {
                    FileUtils.deleteDirectory(tempDir);
                } catch (IOException e) {
                    logger.error("delete temp dir error", e);
                }
            }
        }
    }

    /**
     * 导出为 GeoJson
     *
     * @param geoDataList 数据列表
     * @param response    响应对象
     */
    public static void toGeoJson(List<GeoDataDTO> geoDataList, HttpServletResponse response) {
        File tempDir = null;
        try {
            tempDir = Files.createTempDirectory("geoJson").toFile();

            SimpleFeatureTypeBuilder typeBuilder = createShpSchema();

            // 添加属性字段
            if(!geoDataList.isEmpty()) {
                createPropsByGeoData(typeBuilder, geoDataList.get(0));
            }
            SimpleFeatureType featureType = typeBuilder.buildFeatureType();

            // 1. 构造 SimpleFeatureCollection
            DefaultFeatureCollection featureCollection = createFeatureCollection(geoDataList, featureType);

            String geoJsonPath = tempDir.getAbsolutePath() + File.separator + "data.geojson";
            File geojsonFile = new File(geoJsonPath);
            try (OutputStream os = Files.newOutputStream(geojsonFile.toPath())) {
                FeatureJSON featureJSON = new FeatureJSON();
                featureJSON.writeFeatureCollection(featureCollection, os);
            }
            File zipFile = ZipUtil.zip(tempDir);

            writeZipStream(zipFile, response);
        } catch (Exception e) {
            logger.error("create temp dir error", e);
            throw new VenusException("导出为 GeoJson 失败", e);
        } finally {
            if(tempDir != null) {
                try {
                    FileUtils.deleteDirectory(tempDir);
                } catch (IOException e) {
                    logger.error("delete temp dir error", e);
                }
            }
        }
    }

    /**
     * 写入投影文件（.prj）
     *
     * @param shapefile 文件
     * @param crs       坐标系
     */
    private static void writeProjFile(File shapefile, CoordinateReferenceSystem crs) {
        File prjFile = new File(shapefile.getAbsolutePath().replace(".shp", ".prj"));
        if(crs != null) {
            String wkt = crs.toWKT(); // 将坐标系转换为 WKT 格式
            try (FileWriter writer = new FileWriter(prjFile)) {
                writer.write(wkt);
            } catch (IOException e) {
                throw new VenusException("写入坐标系文件失败", e);
            }
        }
    }

    /**
     * 写出 Zip 文件
     *
     * @param zipFile  Zip 文件
     * @param response 响应对象
     */
    private static void writeZipStream(File zipFile, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/zip");
        response.setContentLengthLong(zipFile.length());
        response.setHeader("Content-Disposition", "attachment; filename=" + "data.zip");

        try (FileInputStream fis = new FileInputStream(zipFile); OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.flush();
        } catch (IOException e) {
            throw new VenusException("写出Zip 文件失败", e);
        }

        zipFile.deleteOnExit();
    }

    /**
     * 抽稀
     *
     * @param dataList 数据列表
     * @param dilution 抽稀比例
     */
    public static List<GeoDataDTO> dilutionGeoJson(List<GeoDataDTO> dataList, double dilution) {
        GeometryJSON geometryJSON = new GeometryJSON();
        for (GeoDataDTO entity : dataList) {
            try {
                Geometry geometry = geometryJSON.read(entity.getGeom());
                Geometry simplifiedGeometry = TopologyPreservingSimplifier.simplify(geometry, dilution);
                String simplifiedGeometryStr = geometryJSON.toString(simplifiedGeometry);
                entity.setGeom(simplifiedGeometryStr);
            } catch (IOException e) {
                throw new VenusException("Java 抽稀是失败", e);
            }
        }
        return dataList;
    }
}
