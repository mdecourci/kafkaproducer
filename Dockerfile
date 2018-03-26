FROM alpine

ARG MSG

ENV MESSAGE ${MSG}

CMD echo "hello world"
CMD echo ${MESSAGE}