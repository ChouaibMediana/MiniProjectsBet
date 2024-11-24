package ca.qc.cgodin.mlg_store.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ca.qc.cgodin.mlg_store.designe_System.LoginPage
import ca.qc.cgodin.mlg_store.designe_System.RegisterPage
import ca.qc.cgodin.mlg_store.designe_System.WelcomePage

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginPage(navController) }
        composable("register") { RegisterPage(navController) }
        composable("welcome") { WelcomePage() }
    }
}
