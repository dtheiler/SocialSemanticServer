FROM learninglayers/base

MAINTAINER Istv�n Koren

RUN apt-get -y install mysql-client

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]