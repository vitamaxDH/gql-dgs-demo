package com.vitamax.gqldgsdemo

import com.jayway.jsonpath.TypeRef
import com.netflix.dgs.codegen.generated.client.CreateUserGraphQLQuery
import com.netflix.dgs.codegen.generated.client.CreateUserProjectionRoot
import com.netflix.dgs.codegen.generated.client.UserGraphQLQuery
import com.netflix.dgs.codegen.generated.client.UserProjectionRoot
import com.netflix.dgs.codegen.generated.types.CreateUserInput
import com.netflix.dgs.codegen.generated.types.GetUserInput
import com.netflix.dgs.codegen.generated.types.User
import com.netflix.graphql.dgs.DgsQueryExecutor
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest
import graphql.scalars.ExtendedScalars
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
internal class UserTest {

    @Autowired
    private lateinit var dgsQueryExecutor: DgsQueryExecutor

    @Test
    fun `generated client classes fail with uuid type field`() {
        val name = "Max"
        val age = 15
        val createRequest =
            GraphQLQueryRequest(
                query = CreateUserGraphQLQuery.Builder().input(
                    CreateUserInput(
                        name = name,
                        age = age
                    )
                ).build(),
                projection = CreateUserProjectionRoot().id().name().age()
            )
        val createdUser = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
            createRequest.serialize(),
            "data.createUser",
            object : TypeRef<User>() {}
        )
        createdUser.id shouldNotBe null
        createdUser.name shouldBe name
        createdUser.age shouldBe age

        val getRequest =
            GraphQLQueryRequest(
                query = UserGraphQLQuery.Builder().input(
                    GetUserInput(
                        id = createdUser.id
                    )
                ).build(),
                projection = UserProjectionRoot().id().name().age(),
            )
        val fetchedUser = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
            getRequest.serialize(),
            "data.user",
            object : TypeRef<User>() {}
        )

        fetchedUser.id shouldBe createdUser.id
        fetchedUser.name shouldBe createdUser.name
        fetchedUser.age shouldBe createdUser.age
    }
}
