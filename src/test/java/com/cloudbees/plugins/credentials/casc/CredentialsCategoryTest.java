package com.cloudbees.plugins.credentials.casc;

import com.cloudbees.plugins.credentials.GlobalCredentialsConfiguration;
import hudson.Extension;
import hudson.ExtensionList;
import io.jenkins.plugins.casc.ConfigurationAsCode;
import io.jenkins.plugins.casc.misc.ConfiguredWithCode;
import io.jenkins.plugins.casc.misc.JenkinsConfiguredWithCodeRule;
import jenkins.model.GlobalConfiguration;
import jenkins.model.GlobalConfigurationCategory;
import org.jenkinsci.Symbol;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.TestExtension;

import javax.annotation.Nonnull;

import java.io.ByteArrayOutputStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class CredentialsCategoryTest {

    @Rule
    public JenkinsConfiguredWithCodeRule r = new JenkinsConfiguredWithCodeRule();

    @Test
    @ConfiguredWithCode("credentials-category.yaml")
    public void globalCredentialsConfigurationCategory() throws Exception {
        assertThat(ExtensionList.lookupSingleton(TestGlobalConfiguration.class).getConfig(), equalTo("hello"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ConfigurationAsCode.get().export(out);
        assertThat(out.toString(), containsString("globalCredentialsConfiguration:\n" +
                "  testGlobalConfiguration:\n" +
                "    config: \"hello\""));
    }

    @TestExtension
    @Symbol("testGlobalConfiguration")
    public static class TestGlobalConfiguration extends GlobalConfiguration {

        private String config;

        @Nonnull
        @Override
        public GlobalConfigurationCategory getCategory() {
            return GlobalConfigurationCategory.get(GlobalCredentialsConfiguration.Category.class);
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Test Global Configuration";
        }

        public String getConfig() {
            return config;
        }

        public void setConfig(String config) {
            this.config = config;
        }
    }
}
