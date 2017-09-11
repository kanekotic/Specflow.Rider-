package kanekotic.specflow.rider;

import gherkin.ast.Feature;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class SpecflowTranslatorCSharpTest
{

    public static final String BodyContent = String.join(
            System.getProperty("line.separator"),
            "[System.CodeDom.Compiler.GeneratedCodeAttribute(\"TechTalk.SpecFlow\", \"1.3.0.0\")]",
            "[System.Runtime.CompilerServices.CompilerGeneratedAttribute()]",
            "%1$s",//nunitExpectedClassAtributes
            "public partial class %2$sFeature",//nameremoving characters and first leter capital
            "{",
            "private static TechTalk.SpecFlow.ITestRunner testRunner;",
            "[NUnit.Framework.TestFixtureSetUpAttribute()]",
            "%3$s",//nunitTextFixtureSetupHeader
            "{",
            "testRunner = TechTalk.SpecFlow.TestRunnerManager.GetTestRunner();",
            "TechTalk.SpecFlow.FeatureInfo featureInfo = new TechTalk.SpecFlow.FeatureInfo(new System.Globalization.CultureInfo(\"en-US\"), \"%4$s\", \"%5$s\", ((string[])(null)));",//name, description
            "testRunner.OnFeatureStart(featureInfo);",
            "}",
            "%6$s",//nunitTextFixtureTearDownHeader
            "{",
            "testRunner.OnFeatureEnd();",
            "testRunner = null;",
            "}",
            "%7$s",//nunitTextScenrioSetupHeader
            "{",
            "testRunner.OnScenarioStart(scenarioInfo);",
            "}",
            "%8$s",//nunitTextScenrioTearDownHeader
            "{",
            "testRunner.OnScenarioEnd();",
            "}",
            "%9$s",//scenarios
            "}");

    private String getRandomString(){
        return UUID.randomUUID().toString();
    }

    @Test
    public void translateGherkingDocumentFeature() {
        SpecflowTranslatorCSharp translator = new SpecflowTranslatorCSharp();

        Feature feature = mock(Feature.class);
        ITestFrameworkConstants constants = mock(ITestFrameworkConstants.class);

        when(feature.getName()).thenReturn(getRandomString().replace('-', ' ') + "("+getRandomString()+"<>!)");
        when(feature.getDescription()).thenReturn(getRandomString().replace('-', ' ') + "("+getRandomString()+"<>!)");
        when(constants.getExpectedClassAtributes()).thenReturn(getRandomString());
        when(constants.getTestFixtureSetupHeader()).thenReturn(getRandomString());
        when(constants.getTestFixtureTearDownHeader()).thenReturn(getRandomString());
        when(constants.getTestScenarioSetupHeader()).thenReturn(getRandomString());
        when(constants.getTestScenarioTearDownHeader()).thenReturn(getRandomString());

        SpecflowFileContents scenarionContent = new SpecflowFileContents();
        scenarionContent.feature = UUID.randomUUID().toString();

        SpecflowFileContents contents = translator.translate(feature, scenarionContent, constants);
        String ExpectedFeatureContent = String.format(BodyContent,
                constants.getExpectedClassAtributes(),
                feature.getName().replaceAll("[^A-Za-z0-9]", ""),
                constants.getTestFixtureSetupHeader(),
                feature.getName(),
                feature.getDescription(),
                constants.getTestFixtureTearDownHeader(),
                constants.getTestScenarioSetupHeader(),
                constants.getTestScenarioTearDownHeader(),
                scenarionContent.feature);
        assertEquals(ExpectedFeatureContent, contents.feature);
        assertEquals("", contents.steps);
    }

}