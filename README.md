
# <a name="demoqa">demoqa</a>
Based on demoqa.com test automation framework example (keys: java, gradle groovy, junit5, ui, api, rest-assured, allure)


## <a name="HowToRun">How to run</a>

### <a name="GradleCommand">Gradle command</a>

To run locally the following command can be is used:

```bash
gradle clean test
```

Additional parameters:
> `-Dthreads=<number_of_threads>` can be added for parallel tests execution\
> `-Denv=remote` can be added for remote tests execution when remote url is set in remote.properties

`-Dtag=<tag>` - tests with this tag will be executed:
>- *api*
>- *ui*


 To get allure report:
```bash
gradle allureServe
```