import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.jvm.tasks.Jar

plugins {
    idea
    alias(libs.plugins.kotlin)
    `java-gradle-plugin`
    `maven-publish`
    signing
}


dependencies {
    implementation(projects.core)
    implementation(projects.processor)
    implementation(libs.kotlin.api)
    implementation(libs.agp.api)
}

val generatedDir = File(projectDir, "generated")
val generatedJavaSourcesDir = File(generatedDir, "main/java")

val genTask = tasks.register("generateBuildClass") {
    inputs.property("version", version)
    outputs.dir(generatedDir)
    doLast {
        val buildClassFile =
            File(generatedJavaSourcesDir, "org/lsposed/lsparanoid/plugin/Build.java")
        buildClassFile.parentFile.mkdirs()
        buildClassFile.writeText(
            """
            package org.lsposed.lsparanoid.plugin;
            /**
             * The type Build.
             */
            public class Build {
               /**
                * The constant VERSION.
                */
               public static final String VERSION = "$version";
            }""".trimIndent()
        )
    }
}

sourceSets {
    main {
        java {
            srcDir(generatedJavaSourcesDir)
        }
    }
}

tasks.withType(KotlinCompile::class.java) {
    dependsOn(genTask)
}

tasks.withType(Jar::class.java) {
    dependsOn(genTask)
}

idea {
    module {
        generatedSourceDirs.add(generatedJavaSourcesDir)
    }
}

publishing {
    repositories {
        maven {
            name = "LSParanoid_Wrapper"
            url = uri("https://maven.pkg.github.com/HosseinZamaniBakhtiarvand/LSParanoid_Wrapper")
            credentials {
                username = project.findProperty("github.username").toString()
                password = project.findProperty("github.token").toString()
            }
        }
    }
}

gradlePlugin {
    plugins {
        create(rootProject.name) {
            val localArtifactId = project.name
            val localGroupId = group
            id = "$localGroupId.$localArtifactId"
            implementationClass = "org.lsposed.lsparanoid.plugin.LSParanoidPlugin"

            description = "String obfuscator for Android applications, clone of https://github.com/LSPosed/LSParanoid"
        }
    }
}
