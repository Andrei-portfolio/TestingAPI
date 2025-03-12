В продолжении к файлу NoSQL, посмотрим ПЯТЫЙ ПУНКТ, как это подружить с JAVA. 

Чтобы работаать с java, есть два способа, первый это в документации на сайте mongodb.com/docs/drivers/java/sync/v4.3/quick-start
найти dependencies код и втавить его в pom.xml, либо идём в mvnrepository.com, находим там MongoDB Driver
(не путать с MongoDB Java Driver (unmaintained) - это не рабочая версия) и скачиваем оттуда 5.2.1

<dependency>
      <groupId>org.mongodb</groupId>
      <artifactId>mongodb-driver-sync</artifactId>
      <version>5.2.1</version>
</dependency>

