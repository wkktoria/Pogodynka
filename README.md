<h1 align="center">weatherreport</h1>

<h3 align="center">Weather application.</h3>

<p align="center">
    <img src="demo.gif"  alt="Demo of application"/> <br>
    • <a href="#run-application">Run Application</a> •
</p>

## Run Application

1. Set environment variable with the API key.

UNIX:

```shell
export WEATHER_API_KEY=YOUR_API_KEY
```

Windows:

```shell
set WEATHER_API_KEY="YOUR_API_KEY"
```

2. Create JAR.

```shell
./mvnw package
```

3. Run JAR.

```shell
java -jar ./target/weatherreport-1.0-SNAPSHOT-jar-with-dependencies.jar
```
