# Temel görüntü olarak OpenJDK kullanımı
FROM openjdk:11-jdk-slim

# Uygulama JAR dosyasının oluşturulacağı dizini tanımla
WORKDIR /app

# Maven bağımlılıklarını önbelleğe almak için bağımlılıkları önce kopyala
COPY target/inventory-0.0.1-SNAPSHOT.jar app.jar

# Uygulamayı başlat
ENTRYPOINT ["java", "-jar", "app.jar"]