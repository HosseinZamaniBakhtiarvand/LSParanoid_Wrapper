plugins {
    alias(libs.plugins.kotlin)
    `maven-publish`
    signing
}

dependencies {
    compileOnly(gradleApi())
    implementation(projects.core)
    implementation(libs.grip)
    implementation(libs.asm.common)
}

publishing {
    publications {
        register<MavenPublication>(rootProject.name) {
            artifactId = project.name
            group = group
            version = version

            from(components.getByName("java"))

            pom {
                name = project.name
                description = "String obfuscator for Android applications, clone of https://github.com/LSPosed/LSParanoid"
                url = "https://github.com/HosseinZamaniBakhtiarvand/LSParanoid_Wrapper"
                licenses {
                    license {
                        name = "Apache License 2.0"
                        url = "https://github.com/HosseinZamaniBakhtiarvand/LSParanoid_Wrapper/blob/master/LICENSE.txt"
                    }
                }
                developers {
                    developer {
                        name = "HosseinZamaniBakhtiarvand"
                        email = "hosseinzamanibakhtiarvand@gmail.com"
                    }
                }
                scm {
                    connection = "scm:git:https://github.com/HosseinZamaniBakhtiarvand/LSParanoid_Wrapper.git"
                    url = "https://github.com/HosseinZamaniBakhtiarvand/LSParanoid_Wrapper"
                }
            }
        }
    }

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
