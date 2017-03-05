[![Build Status](https://travis-ci.org/SeriyBg/junit-easy-tools.svg?branch=master)](https://travis-ci.org/SeriyBg/junit-easy-tools)
[![codecov](https://codecov.io/gh/SeriyBg/junit-easy-tools/branch/master/graph/badge.svg)](https://codecov.io/gh/SeriyBg/junit-easy-tools)

# junit-easy-tools
Extensions for JUnit4

## @DataProducer
Provides lazy calculated parameters inserted to test method.
### Example

```java
@DataProducer
public static Supplier<String> stringProducer = () -> RandomStringProvider.get();

@Test
public void testMethod(String string) {
   ...
}
```
