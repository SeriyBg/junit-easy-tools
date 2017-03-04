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
