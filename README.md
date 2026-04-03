package com.carnival.protaskflowv;

import com.carnival.protaskflowv.config.ApplicationProperties;
import com.carnival.protaskflowv.config.Constants;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import tech.jhipster.config.DefaultProfileUtil;
import tech.jhipster.config.JHipsterConstants;

/**
 * The main application class for Protaskflowv.
 * <p>
 * This class serves as the entry point for the Spring Boot application.
 * It initializes the application context, loads configurations, and sets up
 * the necessary environment for Protaskflowv to run.
 * </p>
 * <p>
 * It is responsible for:
 * <ul>
 *     <li>Bootstrapping the Spring application.</li>
 *     <li>Handling Spring profile activation (e.g., 'dev', 'prod', 'cloud').</li>
 *     <li>Logging essential startup information like application URLs and active profiles.</li>
 *     <li>Enabling configuration properties from {@link ApplicationProperties} and {@link LiquibaseProperties}.</li>
 * </ul>
 * </p>
 * <p>
 * To start the application, simply run the {@code main} method in this class.
 * </p>
 *
 * @see ProtaskflowvApp#main(String[])
 * @see ApplicationProperties
 * @see LiquibaseProperties
 * @see Constants
 * @see DefaultProfileUtil
 * @see JHipsterConstants
 */
@SpringBootApplication
@EnableConfigurationProperties({ LiquibaseProperties.class, ApplicationProperties.class })
public class ProtaskflowvApp {

    private static final Logger log = LoggerFactory.getLogger(ProtaskflowvApp.class);

    private final Environment env;

    /**
     * Constructor for ProtaskflowvApp, injecting the Spring Environment.
     *
     * @param env The Spring {@link Environment} used to access application properties and profiles.
     */
    public ProtaskflowvApp(Environment env) {
        this.env = env;
    }

    /**
     * Initializes the Protaskflowv application.
     * <p>
     * This method is called automatically after the application context has been constructed
     * and before the application starts accepting requests. It performs important checks
     * related to Spring profile configuration to prevent common misconfigurations.
     * </p>
     * <p>
     * Spring profiles can be configured via command-line arguments (e.g., {@code --spring.profiles.active=dev})
     * or environment variables.
     * </p>
     * <p>
     * For more information on JHipster profiles, refer to the official documentation:
     * <a href="https://www.jhipster.tech/profiles/">https://www.jhipster.tech/profiles/</a>.
     * </p>
     */
    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) &&
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)
        ) {
            log.error(
                "You have misconfigured your application! It should not run " + "with both the 'dev' and 'prod' profiles at the same time."
            );
        }
        if (
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) &&
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_CLOUD)
        ) {
            log.error(
                "You have misconfigured your application! It should not run " + "with both the 'dev' and 'cloud' profiles at the same time."
            );
        }
    }

    /**
     * Main method, used to run the Protaskflowv application.
     * <p>
     * This is the standard entry point for a Spring Boot application.
     * It sets up default profiles and then runs the application, logging
     * useful information about its startup.
     * </p>
     *
     * @param args The command line arguments passed to the application.
     * @throws UnknownHostException If the local host name cannot be resolved into an address,
     *                              which is used for logging the application's external URL.
     */
    public static void main(String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(ProtaskflowvApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    /**
     * Logs the application startup information to the console.
     * <p>
     * This private helper method extracts relevant details from the Spring {@link Environment}
     * and displays them in a user-friendly format, including local and external access URLs,
     * and active Spring profiles. It also attempts to log the status of the Config Server
     * if applicable.
     * </p>
     *
     * @param env The Spring {@link Environment} from which to retrieve application details.
     */
    private static void logApplicationStartup(Environment env) {
        String protocol = Optional.ofNullable(env.getProperty("server.ssl.key-store")).map(key -> "https").orElse("http");
        String applicationName = env.getProperty("spring.application.name");
        String serverPort = env.getProperty("server.port");
        String contextPath = Optional
            .ofNullable(env.getProperty("server.servlet.context-path"))
            .filter(StringUtils::isNotBlank)
            .orElse("/");
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info(
            "\n----------------------------------------------------------\n\t" +
            "Application '{}' is running! Access URLs:\n\t" +
            "Local: \t\t{}://localhost:{}{}\n\t" +
            "External: \t{}://{}:{}{}\n\t" +
            "Profile(s): \t{}\n----------------------------------------------------------",
            applicationName,
            protocol,
            serverPort,
            contextPath,
            protocol,
            hostAddress,
            serverPort,
            contextPath,
            env.getActiveProfiles().length == 0 ? env.getDefaultProfiles() : env.getActiveProfiles()
        );

        String configServerStatus = env.getProperty("configserver.status");
        if (configServerStatus == null) {
            configServerStatus = "Not found or not setup for this application";
        }
        log.info(
            "\n----------------------------------------------------------\n\t" +
            "Config Server: \t{}\n----------------------------------------------------------",
            configServerStatus
        );
    }
}