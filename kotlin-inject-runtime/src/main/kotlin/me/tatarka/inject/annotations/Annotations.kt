package me.tatarka.inject.annotations

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*

@Retention(RUNTIME)
@Target(CLASS)
annotation class Inject

@Retention(RUNTIME)
@Target(CLASS)
annotation class Module

@Retention(RUNTIME)
@Target(ANNOTATION_CLASS)
annotation class Scope

@Retention(RUNTIME)
@Target(FUNCTION, PROPERTY_GETTER)
annotation class IntoSet

@Retention(RUNTIME)
@Target(FUNCTION, PROPERTY_GETTER)
annotation class IntoMap
