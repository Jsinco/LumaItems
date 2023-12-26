package dev.jsinco.lumaitems.hooks

import me.clip.placeholderapi.expansion.PlaceholderExpansion

class PAPIManager {

    val placeholderClasses: MutableList<PlaceholderExpansion> = mutableListOf()

    fun registerPlaceholders() {
        for (placeholderClass in placeholderClasses) {
            placeholderClass.register()
        }
    }

    fun unregisterPlaceholders() {
        for (placeholderClass in placeholderClasses) {
            placeholderClass.unregister()
        }
    }

    fun addPlaceholder(placeholderClass: PlaceholderExpansion) {
        placeholderClasses.add(placeholderClass)
    }

    fun removePlaceholder(placeholderClass: PlaceholderExpansion) {
        placeholderClasses.remove(placeholderClass)
    }

    fun hasRegisteredPlaceholders(): Boolean {
        return placeholderClasses.isNotEmpty()
    }
}