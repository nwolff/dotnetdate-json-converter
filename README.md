# dotnetdate-json-converter

[![wercker status](https://app.wercker.com/status/3596d5cb161a2a7446eb7ac97fe853ab/s "wercker status")](https://app.wercker.com/project/bykey/3596d5cb161a2a7446eb7ac97fe853ab)

## Goal

The goal is to integrate with .net json services. Those serialize dates in a very specific format: https://msdn.microsoft.com/en-us/library/bb412170%28v=vs.110%29.aspx

This library helps convert dates to/from that format.

Adapters for Gson, Jackson (or any json serialization library) are easy to write on top of this library.


## Requirements

Uses the java.time classes introduced in java 8


## Obtaining the jar

Use jitpack to obtain the latest build: https://jitpack.io/

For instance if you are using maven:

```
<dependency>
    <groupId>com.github.nwolff</groupId>
    <artifactId>dotnetdate-json-converter</artifactId>
     <version>-SNAPSHOT</version>
</dependency>
```

