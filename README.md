[![Build Status](https://travis-ci.org/SeriyBg/junit-easy-tools.svg?branch=master)](https://travis-ci.org/SeriyBg/junit-easy-tools)
[![codecov](https://codecov.io/gh/SeriyBg/junit-easy-tools/branch/master/graph/badge.svg)](https://codecov.io/gh/SeriyBg/junit-easy-tools)
[![Dependency Status](https://www.versioneye.com/user/projects/58bbf75f2ff683004468cc9f/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/58bbf75f2ff683004468cc9f)

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
