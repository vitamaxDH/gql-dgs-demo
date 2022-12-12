package com.vitamax.gqldgsdemo

import com.netflix.dgs.codegen.generated.types.CreateUserInput
import com.netflix.dgs.codegen.generated.types.GetUserInput
import com.netflix.dgs.codegen.generated.types.User
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import java.util.*

@DgsComponent
class UserFetcher {

    private val userList = mutableListOf<User>()
    @DgsMutation
    fun createUser(@InputArgument input: CreateUserInput): User {
        val user = User(
            id = UUID.randomUUID(),
            name = input.name,
            age = input.age
        )
        userList.add(
            user
        )
        return user
    }

    @DgsQuery
    fun user(@InputArgument input: GetUserInput): User? {
        return userList.find { it.id == input.id }
    }

}
