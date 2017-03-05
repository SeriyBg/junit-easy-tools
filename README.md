[![Build Status](https://travis-ci.org/SeriyBg/junit-easy-tools.svg?branch=master)](https://travis-ci.org/SeriyBg/junit-easy-tools)
[![codecov](https://codecov.io/gh/SeriyBg/junit-easy-tools/branch/master/graph/badge.svg)](https://codecov.io/gh/SeriyBg/junit-easy-tools)
[![Dependency Status](https://www.versioneye.com/user/projects/58bbf75f2ff683004468cc9f/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/58bbf75f2ff683004468cc9f)

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
