package com.example.trp.domain.navigation.common

/*
@Composable
fun AuthNavGraph(
    destination: String = AuthScreen.Login.route,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        route = Graph.AUTHENTICATION,
        startDestination = destination
    ) {
        composable(route = AuthScreen.Login.route) {
            navController.currentDestination?.hierarchy?.forEach {
                Log.e("AuthNavGraph it.route", it.route.toString())
            }
            Log.e("AuthNavGraph it.route", "\n")
            LoginScreen(onLoginClick = { destination ->
                navController.navigate(destination)
            })
        }
        composable(route = Graph.STUDENT_WELCOME) {
            StudentWelcomeScreen()
        }
        composable(route = Graph.TEACHER_WELCOME) {
            TeacherWelcomeScreen()
        }
        composable(route = Graph.ADMIN_WELCOME) {
            AdminWelcomeScreen()
        }
    }
}

sealed class AuthScreen(val route: String) {
    object Login : AuthScreen(route = "LOGIN")
}*/
