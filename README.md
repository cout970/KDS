# Kotlin Dynamic Scripting (KDS)
This is an experiment to explore the possibility mods that are fully hotswappable.

## What does it look like?

Check the example mod [here](src/main/kotlin/example_mod/scripts)

## Why Kotlin
This project would be impractical without Kotlin.
Kotlin has a bunch of features that Java lacks that makes it perfect for this.

Feature in Kotlin that are not in Java:
- Type inference
- Nonnull Types
- Extension Functions
- DSL
- Kotlin Scripting
- Classless functions
- Automatic getters/setters
- Data classes
- Much more

All this features and more are used to make a great modding experience, mitigating the pain of modding Minecraft.

### Type inference
In kotlin a variable can be defined as
```kotlin
var myVariable = "MyValue"
```
Notice you don't need to say that the value is a String

### Nonnull Types
In Kotlin you can define if a type allows null or not. 
The question mark at the end indicates that the value allows nulls. For example:
```kotlin
var myVariable: String
var myNullableVariable: String?
```
myVariable does not allow the null value and myNullableVariable does.
`myVariable = null` would generate a compilation error.

### Extension functions
Kotlin allows you to create functions the act as methods of other classes.
For example: `list.isNotEmpty()` is an extension, the Collection interface does not have that method, 
instead in the stdlib there is this function:
```kotlin
fun <T> Collection<T>.isNotEmpty(): Boolean = !isEmpty()
```
The function extends the Collection interface adding isNotEmpty, this can be done for any class or interface to make your code cleaner.
If we wanted to do this in java we would need to do `CollectionUtils.isNotEmpty(list)` which doesn't make use of autocomplete,
 you need to remember all you utility classes and is more verbose.
 
### DSL
DSL (Domain Specific Language) is a way to model and define data of a specific domain using the features the language provides.
For example to define an item model we could use the following DSL:
```kotlin
model {
    parent = "item/generated"
    textures {
        texture("layer0", "example_mod:item/custom_texture")
    }
}
```
Json equivalent:
```json
{
  "parent": "item/generated",
  "textures": {
    "layer0": "example_mod:item/custom_texture"
  }
}
```
The code is completely valid Kotlin, like the json counterpart, it defines a simple item model,
 but the Kotlin code has some advantages:
- Autocomplete: inside `model{...}` the IDE will recommend and autocomplete `parent` and `textures{}`.
- Compile time validation: if you write `paernt` you will get a compilation error, json will give you an error once the game finish loading.
- Access to the full language: you can use loops, conditionals and reuse code with functions, avoiding duplicating the same data hundreds of times.
- It is not JSON: you can auto-generate everything with code if you want, you are not being limited to the static structure of JSON.

### Kotlin Scripting
Kotlin supports multiple targets JVM, JS, Native and Scripting.
We can use Kotlin scripts to allow instant hotswap of code even if the class structure has changed,
this mean you can change anything in you code and see the changes in-game instantly without the need of restating the game.

You can also use regular Kotlin files, or a mix of scripts and compiled code, if you want a more traditional approach,
 all the APIs are still fully available.

Note: Kotlin scripting compiles the code to decent JVM bytecode, which means there isn't a big performance penalty, 
like in other scripting language options.


### Classless functions
You can define functions outside classes, for example 'print(...)' and 'println(...)' are classless functions, 
you can use them everywhere, no more 'System.out.println()'.

### Automatic getters/setters
When you define a public property in a class Kotlin will generate getters and setters for you in the compiled bytecode.

Also, Kotlin allow you to use getters and setters with the same syntax as public fields.
`user.name` becomes `user.getName()` in the compiled bytecode.

You can also use [] to access maps and lists, `userMap["key"]` becomes 'userMap.get("key")'

### Data classes
Define classes with only 1 line
```kotlin
data class Node(val parent: Node?, val value: String) 
```
Adding `data` before the class keyword will add toString(), hashCode(), equals(...), copy(...) and componentN(...).
This saves you from generating all this boilerplate code.

Notice that we have the constructor and list of fields in the same line using a Primary constructor, this is equivalent to:
```kotlin
data class Node {
    val parent: Node?
    val value: String
    
    constructor(parent: Node?, value: String) {
        this.parent = parent
        this.value = value
    }
} 
```

### Much more
If after reading this section you are interested in learning Kotlin and all of it's amazing features,
we recommend using [Kotlin Koans](https://play.kotlinlang.org/byExample/01_introduction/01_Hello%20world), 
It's a list of interactive tutorials that walks you through the language, teaches you the basics and 
gives you challenges to show that you are really learning.

## Status
Currently, basic features work but there are a lot of limitations: no inline, you can't expose classless functions of 
a script, you need to call Scripting.include() to use content in another script, include() is async (executed after the 
current scrip has finished), no debugging in script, line numbers are erased at runtime, etc.
Also, there are several errors in the script engine, sometimes when a script is re-evaluated it generates a compiler 
frontend error.

The Kotlin compiler is under a rewrite to increase performance, allow compiler plugins and allow better customization in 
the script engine. 
This is scheduled for Kotlin 1.4 but may take a while more to get stable enough for this use case. 
