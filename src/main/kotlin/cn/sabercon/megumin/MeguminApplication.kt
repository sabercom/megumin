package cn.sabercon.megumin

import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan

@ComponentScan("cn.sabercon")
@SpringBootApplication
class MeguminApplication {

    @Bean
    fun jobRunner() = ApplicationRunner {
        // To run some ad-hoc tasks
    }
}

fun main(vararg args: String) {
    runApplication<MeguminApplication>(*args)
}
