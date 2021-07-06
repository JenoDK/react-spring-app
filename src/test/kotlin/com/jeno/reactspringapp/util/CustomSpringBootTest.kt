package com.jeno.reactspringapp.util

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@Retention
@Target(AnnotationTarget.CLASS)
@SpringBootTest
@ActiveProfiles("test")
annotation class CustomSpringBootTest()
