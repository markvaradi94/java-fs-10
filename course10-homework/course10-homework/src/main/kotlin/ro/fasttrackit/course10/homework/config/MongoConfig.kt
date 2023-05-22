package ro.fasttrackit.course10.homework.config

import com.mongodb.client.MongoClients
import org.apache.logging.log4j.util.Strings.EMPTY
import org.jasypt.encryption.StringEncryptor
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor
import org.jasypt.iv.RandomIvGenerator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory

private const val s = "<DECRYPTED_USER>"

@Configuration
class MongoConfig {
    @Value("\${spring.data.mongodb.uri}")
    private val mongoUri: String = EMPTY

    @Value("\${spring.data.mongodb.database}")
    private val database: String = EMPTY

    @Value("\${mongodb.encryptedPassword}")
    private val encryptedPassword: String = EMPTY

    @Value("\${mongodb.encryptedUsername}")
    private val encryptedUsername: String = EMPTY

    @Bean
    fun jasyptStringEncryptor(): StringEncryptor {
        val encryptor = StandardPBEStringEncryptor()
        encryptor.setAlgorithm("PBEWithHMACSHA512AndAES_256")
        encryptor.setIvGenerator(RandomIvGenerator())
        encryptor.setPassword(System.getenv("JASYPT_ENCRYPTOR_PASSWORD"))
        return encryptor
    }

    @Bean
    fun mongoDatabaseFactory(jasyptStringEncryptor: StringEncryptor): MongoDatabaseFactory {
        val decryptedPassword = jasyptStringEncryptor.decrypt(encryptedPassword)
        val decryptedUsername = jasyptStringEncryptor.decrypt(encryptedUsername)
        val uriWithUser = mongoUri.replace("<DECRYPTED_USER>", decryptedUsername)
        val connectionString = uriWithUser.replace("<DECRYPTED_PASSWORD>", decryptedPassword)
        val mongoClient = MongoClients.create(connectionString)
        return SimpleMongoClientDatabaseFactory(mongoClient, database)
    }

    @Bean
    fun mongoTemplate(mongoDatabaseFactory: MongoDatabaseFactory) = MongoTemplate(mongoDatabaseFactory)
}
