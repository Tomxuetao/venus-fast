FROM nginx:alpine

RUN mkdir /data

COPY dist/ /data/venus-fast

CMD ["nginx", "-g", "daemon off;"]
