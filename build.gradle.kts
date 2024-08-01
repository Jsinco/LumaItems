import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.0.20-RC"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.papermc.paperweight.userdev") version "1.7.1" // PaperWeight
}

val javaVers = 21
val projectName = "LumaItems"
group = "dev.jsinco"
version = "1.0.0"


repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://nexus.iridiumdevelopment.net/repository/maven-releases/")
    maven("https://jitpack.io")
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://mvn.lumine.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.3")
    compileOnly("com.github.Zrips:jobs:v4.17.2")
    compileOnly("com.comphenix.protocol:ProtocolLib:5.0.0")
    compileOnly("io.lumine:Mythic-Dist:5.6.1")

    implementation("com.iridium:IridiumColorAPI:1.0.9")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // PaperWeight
    paperweight.paperDevBundle("1.21-R0.1-SNAPSHOT")
}

tasks {
    java {
        toolchain.languageVersion = JavaLanguageVersion.of(javaVers)
    }

    assemble {
        dependsOn(reobfJar)
    }

    processResources {
        outputs.upToDateWhen { false }
        filter<ReplaceTokens>(mapOf(
            "tokens" to mapOf("version" to project.version.toString()),
            "beginToken" to "\${",
            "endToken" to "}"
        ))
    }

    shadowJar {
        dependencies {
            include(dependency("com.iridium:IridiumColorAPI"))
            include(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
        }
        archiveClassifier.set("")
    }

    jar {
        version = ""
        enabled = false
    }

    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    kotlin {
        jvmToolchain(javaVers)
    }

    reobfJar {
        outputJar.set(layout.buildDirectory.file("${projectDir}${File.separator}build${File.separator}libs${File.separator}${project.name}.jar"))
    }

    build {
        dependsOn(shadowJar)
        //dependsOn("copyJar")
    }
}



/*

paperweight {
    jar.archiveClassifier
}

def targetJavaVersion = 17
java {
    def javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    }
}

tasks.withType(JavaCompile).configureEach {
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible()) {
        options.release.set(targetJavaVersion)
    }
}

processResources {
    def props = [version: version]
    inputs.properties props
    filteringCharset 'UTF-8'
    filesMatching('plugin.yml') {
        expand props
    }
}

tasks.withType(JavaCompile).configureEach {
    options.encoding = 'UTF-8'
}

kotlin {
    jvmToolchain(17)
}

// PaperWeight
tasks.assemble {
    dependsOn(reobfJar)
}

jar {
    version = null
    enabled = true
}

shadowJar {
    dependencies {
        include(dependency("com.iridium:IridiumColorAPI"))
        include(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
    }
    archiveClassifier.set('')
}

// PaperWeight
reobfJar {
    outputJar.set(layout.buildDirectory.file("${projectDir}\\build\\libs\\${projectName}.jar"))
}

/*
tasks.register('copyJar', Copy) {
    from reobfJar
    into "C:\\Users\\jonah\\paper-server\\plugins"
}
 */


build {
    dependsOn shadowJar
    //dependsOn("copyJar")
}
 */
