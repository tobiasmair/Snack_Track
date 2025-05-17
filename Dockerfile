# -------------------------
# 🏗️  Build Stage
# -------------------------
FROM maven:3.8.1-openjdk-17-slim AS build

RUN curl -sL https://deb.nodesource.com/setup_14.x | bash - && \
    apt-get update -qq && \
    apt-get install -qq --no-install-recommends nodejs

RUN useradd -m myuser
WORKDIR /usr/src/app/
RUN chown myuser:myuser /usr/src/app/
USER myuser

COPY --chown=myuser pom.xml ./
RUN mvn dependency:go-offline -Pproduction

COPY --chown=myuser:myuser src src
COPY --chown=myuser:myuser src/main/frontend frontend

RUN mvn clean package -DskipTests -Pproduction \
    -Dvaadin.offlineKey=${VAADIN_OFFLINE_KEY}

# -------------------------
# 🚀 Run Stage - To the Moon ->
# -------------------------
FROM maven:3.8.1-openjdk-17-slim

RUN curl -sL https://deb.nodesource.com/setup_14.x | bash - && \
    apt-get update -qq && \
    apt-get install -qq --no-install-recommends nodejs

RUN useradd -m myuser
USER myuser

COPY --from=build /usr/src/app/target/*.jar /usr/app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/usr/app/app.jar"]
