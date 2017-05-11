[![Codacy Badge](https://api.codacy.com/project/badge/Grade/edbdf73dad50403e8be8dfde05cb4107)](https://www.codacy.com/app/SeriyBg/junit-easy-tools?utm_source=github.com&utm_medium=referral&utm_content=SeriyBg/junit-easy-tools&utm_campaign=badger)
[![Build Status](https://travis-ci.org/SeriyBg/junit-easy-tools.svg?branch=master)](https://travis-ci.org/SeriyBg/junit-easy-tools)
[![codecov](https://codecov.io/gh/SeriyBg/junit-easy-tools/branch/master/graph/badge.svg)](https://codecov.io/gh/SeriyBg/junit-easy-tools)
[![Dependency Status](https://www.versioneye.com/user/projects/58bbf75f2ff683004468cc9f/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/58bbf75f2ff683004468cc9f)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/edbdf73dad50403e8be8dfde05cb4107)](https://www.codacy.com/app/SeriyBg/junit-easy-tools?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=SeriyBg/junit-easy-tools&amp;utm_campaign=Badge_Grade)

# junit-easy-tools
Extensions for JUnit4

## @DataProducer
Provides lazy calculated parameters inserted to test method.
### Example

#### Simple example
The value of `stringProducer` will be calculated and injected to the `testMethod(String string)`
```java
@DataProducer
public static Supplier<String> stringProducer = () -> RandomStringProvider.get();

@Test
public void testMethod(String string) {
   ...
}
```

#### Example with iterations
The `testMethod(String string)` will run three times (`@ProducedValues(iterations = 3)`).
Each run the value of `stringProducer` will be recalculated, so the new value will be provided. 
```java
@DataProducer
public static Supplier<String> stringProducer = () -> RandomStringProvider.get();

@Test
@ProducedValues(iterations = 3)
public void testMethod(String string) {
   ...
}
```

#### Named @DataProducer
To `testMethod(@ProducedValue(producer = "calculated") String string)` will be injected values from `calculatedProducer`.
If no `name` is specified for `@DataProducer`, the field name will be considered as `@DataProducer` name.
```java
@DataProducer
public static Supplier<String> randomProducer = () -> RandomStringProvider.get();

@DataProducer(name = "calculated")
public static Supplier<String> calculatedProducer = () -> CalculatedStringProvider.get();

@Test
public void testMethod(@ProducedValue(producer = "calculated") String string) {
   ...
}
```

#### Multiple @DataProducer of the same type
Method `test(String s)` will be executed twice. 
The first time value from `random` `@DataProducer` will be injected.
The second time value from `queue` `@DataProducer` will be injected. 
```java
@DataProducer
public static Supplier<String> random = () -> RandomStringProvider.get();

@DataProducer
public static Supplier<String> queue = () -> StringQueue.next();
 
@Test
public void test(String s) {
    ...
}
```

#### Multiple @DataProducer of the same type and iterations
Method `test(String s)` will be executed six times.
Three iterations with each `@DataProducer`.

```java
@DataProducer
public static Supplier<String> random = () -> RandomStringProvider.get();

@DataProducer
public static Supplier<String> queue = () -> StringQueue.next();

@Test
@ProducedValues(iterations = 3)
public void test(String s) {
    ...
}
```
