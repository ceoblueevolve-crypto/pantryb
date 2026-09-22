package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.i18n.LocalizationManager
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.PantryViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  private val viewModel: PantryViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        PantryApp(viewModel = viewModel)
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantryApp(viewModel: PantryViewModel) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val isUnlocked by viewModel.biometricManager.isUnlocked.collectAsStateWithLifecycle()
  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val scope = rememberCoroutineScope()
  var isHomeInventoryExpanded by remember { mutableStateOf(false) }
  var isSettingsMenuExpanded by remember { mutableStateOf(false) }
  val strings = LocalizationManager.getStrings(uiState.selectedLanguage)

  // Handle hardware / gesture back button: go back to Inventory tab (0)
  BackHandler(enabled = uiState.activeTab != 0) {
    viewModel.selectTab(0)
  }

  // If locked, show Biometric Authentication Screen
  AnimatedContent(
    targetState = isUnlocked,
    label = "lock_state_transition"
  ) { unlocked ->
    if (!unlocked) {
      BiometricLockScreen(viewModel = viewModel)
    } else {
      ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
          ModalDrawerSheet(
            modifier = Modifier
              .widthIn(max = 320.dp)
              .fillMaxHeight(),
            drawerContainerColor = MaterialTheme.colorScheme.surface,
            drawerTonalElevation = 3.dp
          ) {
            Column(
              modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
            ) {
              // Drawer Header Branding
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                  .padding(horizontal = 24.dp, vertical = 24.dp)
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                  Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(46.dp)
                  ) {
                    Box(contentAlignment = Alignment.Center) {
                      Icon(
                        Icons.Default.Kitchen,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(26.dp)
                      )
                    }
                  }
                  Column {
                    Text(
                      text = strings.drawerHeaderTitle,
                      style = MaterialTheme.typography.titleMedium,
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                      text = strings.drawerHeaderSubtitle,
                      style = MaterialTheme.typography.bodySmall,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                  shape = RoundedCornerShape(20.dp),
                  color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                  ) {
                    Box(
                      modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                    )
                    Text(
                      text = "${strings.activeProfile}: ${uiState.activeFamilyMember}",
                      style = MaterialTheme.typography.labelMedium,
                      fontWeight = FontWeight.SemiBold
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(12.dp))

              Text(
                text = strings.mainNavigation,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
              )

              // Inventory Drawer Item
              NavigationDrawerItem(
                icon = {
                  Icon(
                    if (uiState.activeTab == 0) Icons.Filled.Kitchen else Icons.Outlined.Kitchen,
                    contentDescription = null
                  )
                },
                label = { Text(strings.navInventory) },
                selected = uiState.activeTab == 0,
                onClick = {
                  viewModel.selectTab(0)
                  scope.launch { drawerState.close() }
                },
                modifier = Modifier
                  .padding(NavigationDrawerItemDefaults.ItemPadding)
                  .testTag("drawer_item_inventory")
              )

              Spacer(modifier = Modifier.height(4.dp))

              // Collapsible "My Home Inventory" Parent Menu
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 14.dp)
                  .clip(RoundedCornerShape(12.dp))
                  .clickable { isHomeInventoryExpanded = !isHomeInventoryExpanded }
                  .padding(horizontal = 12.dp, vertical = 10.dp)
                  .testTag("drawer_home_inventory_collapse_toggle"),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  Icons.Default.Home,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                  text = strings.myHomeInventory,
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.weight(1f)
                )
                Icon(
                  imageVector = if (isHomeInventoryExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                  contentDescription = if (isHomeInventoryExpanded) "Collapse" else "Expand",
                  tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }

              // Animated Sub-items under "My Home Inventory" (In order: Storage Locations, Product Categories, Family Members, Family Shopp List, Restock Alerts)
              AnimatedVisibility(
                visible = isHomeInventoryExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
              ) {
                Column(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
                    .testTag("drawer_home_inventory_submenu")
                ) {
                  // 1. Storage Locations
                  NavigationDrawerItem(
                    icon = {
                      Icon(
                        if (uiState.activeTab == 2) Icons.Filled.Place else Icons.Outlined.Place,
                        contentDescription = null
                      )
                    },
                    label = { Text(strings.navLocations) },
                    selected = uiState.activeTab == 2,
                    onClick = {
                      viewModel.selectTab(2)
                      scope.launch { drawerState.close() }
                    },
                    modifier = Modifier
                      .padding(NavigationDrawerItemDefaults.ItemPadding)
                      .testTag("drawer_item_locations")
                  )

                  // 2. Temperature Zones
                  NavigationDrawerItem(
                    icon = {
                      Icon(
                        if (uiState.activeTab == 7) Icons.Filled.DeviceThermostat else Icons.Outlined.DeviceThermostat,
                        contentDescription = null
                      )
                    },
                    label = { Text(strings.navTempZones) },
                    selected = uiState.activeTab == 7,
                    onClick = {
                      viewModel.selectTab(7)
                      scope.launch { drawerState.close() }
                    },
                    modifier = Modifier
                      .padding(NavigationDrawerItemDefaults.ItemPadding)
                      .testTag("drawer_item_temp_zones")
                  )

                  // 3. Product Categories
                  NavigationDrawerItem(
                    icon = {
                      Icon(
                        if (uiState.activeTab == 1) Icons.Filled.Category else Icons.Outlined.Category,
                        contentDescription = null
                      )
                    },
                    label = { Text(strings.navCategories) },
                    selected = uiState.activeTab == 1,
                    onClick = {
                      viewModel.selectTab(1)
                      scope.launch { drawerState.close() }
                    },
                    modifier = Modifier
                      .padding(NavigationDrawerItemDefaults.ItemPadding)
                      .testTag("drawer_item_categories")
                  )

                  // 4. Family Members
                  NavigationDrawerItem(
                    icon = {
                      Icon(
                        if (uiState.activeTab == 6) Icons.Filled.People else Icons.Outlined.People,
                        contentDescription = null
                      )
                    },
                    label = { Text(strings.navFamily) },
                    selected = uiState.activeTab == 6,
                    onClick = {
                      viewModel.selectTab(6)
                      scope.launch { drawerState.close() }
                    },
                    modifier = Modifier
                      .padding(NavigationDrawerItemDefaults.ItemPadding)
                      .testTag("drawer_item_family")
                  )

                  // 5. Family Shopping List
                  NavigationDrawerItem(
                    icon = {
                      Icon(
                        if (uiState.activeTab == 3) Icons.Filled.ShoppingCart else Icons.Outlined.ShoppingCart,
                        contentDescription = null
                      )
                    },
                    label = { Text(strings.navShopping) },
                    selected = uiState.activeTab == 3,
                    onClick = {
                      viewModel.selectTab(3)
                      scope.launch { drawerState.close() }
                    },
                    modifier = Modifier
                      .padding(NavigationDrawerItemDefaults.ItemPadding)
                      .testTag("drawer_item_shopping")
                  )

                  // 6. Restock Alerts
                  NavigationDrawerItem(
                    icon = {
                      Icon(
                        if (uiState.activeTab == 4) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                        contentDescription = null
                      )
                    },
                    label = { Text(strings.navAlerts) },
                    selected = uiState.activeTab == 4,
                    onClick = {
                      viewModel.selectTab(4)
                      scope.launch { drawerState.close() }
                    },
                    modifier = Modifier
                      .padding(NavigationDrawerItemDefaults.ItemPadding)
                      .testTag("drawer_item_alerts")
                  )
                }
              }

              Spacer(modifier = Modifier.height(8.dp))
              HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))
              Spacer(modifier = Modifier.height(8.dp))

              // Vertical Collapsible General Settings Parent Menu
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 14.dp)
                  .clip(RoundedCornerShape(12.dp))
                  .clickable { isSettingsMenuExpanded = !isSettingsMenuExpanded }
                  .padding(horizontal = 12.dp, vertical = 10.dp)
                  .testTag("drawer_settings_collapse_toggle"),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  Icons.Default.Settings,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = strings.settingsSubmenuTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = strings.settingsSubmenuDesc,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
                Icon(
                  imageVector = if (isSettingsMenuExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                  contentDescription = if (isSettingsMenuExpanded) "Collapse Settings" else "Expand Settings",
                  tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }

              // Animated Sub-items under General Settings Menu
              AnimatedVisibility(
                visible = isSettingsMenuExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
              ) {
                Column(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
                    .testTag("drawer_settings_submenu")
                ) {
                  // General Settings Submenu Item calling General Settings Page
                  NavigationDrawerItem(
                    icon = {
                      Icon(
                        if (uiState.activeTab == 5) Icons.Filled.Tune else Icons.Outlined.Tune,
                        contentDescription = null,
                        tint = if (uiState.activeTab == 5)
                          MaterialTheme.colorScheme.primary
                        else
                          MaterialTheme.colorScheme.onSurfaceVariant
                      )
                    },
                    label = { Text(strings.navSettings) },
                    selected = uiState.activeTab == 5,
                    onClick = {
                      viewModel.selectTab(5)
                      scope.launch { drawerState.close() }
                    },
                    modifier = Modifier
                      .padding(NavigationDrawerItemDefaults.ItemPadding)
                      .testTag("drawer_item_general_settings")
                  )

                  // About App Drawer Item
                  NavigationDrawerItem(
                    icon = {
                      Icon(
                        if (uiState.activeTab == 8) Icons.Filled.Info else Icons.Outlined.Info,
                        contentDescription = null,
                        tint = if (uiState.activeTab == 8)
                          MaterialTheme.colorScheme.primary
                        else
                          MaterialTheme.colorScheme.onSurfaceVariant
                      )
                    },
                    label = { Text(strings.navAboutApp) },
                    selected = uiState.activeTab == 8,
                    onClick = {
                      viewModel.selectTab(8)
                      scope.launch { drawerState.close() }
                    },
                    modifier = Modifier
                      .padding(NavigationDrawerItemDefaults.ItemPadding)
                      .testTag("drawer_item_about_app")
                  )
                }
              }

              Spacer(modifier = Modifier.height(16.dp))
              HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))
              Spacer(modifier = Modifier.height(12.dp))

              // Drawer Footer with Quick Lock
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                OutlinedButton(
                  onClick = {
                    scope.launch { drawerState.close() }
                    viewModel.lockApp()
                  },
                  modifier = Modifier
                    .fillMaxWidth()
                    .testTag("drawer_quick_lock_button")
                ) {
                  Icon(
                    Icons.Default.Lock,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(strings.lockApp)
                }
              }

              Spacer(modifier = Modifier.height(16.dp))
            }
          }
        }
      ) {
        Scaffold(
          modifier = Modifier
            .fillMaxSize()
            .testTag("main_app_scaffold"),
          topBar = {
            TopAppBar(
              navigationIcon = {
                if (uiState.activeTab != 0) {
                  IconButton(
                    onClick = { viewModel.selectTab(0) },
                    modifier = Modifier.testTag("top_bar_back_button")
                  ) {
                    Icon(
                      imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                      contentDescription = "Back to inventory"
                    )
                  }
                } else {
                  IconButton(
                    onClick = { scope.launch { drawerState.open() } },
                    modifier = Modifier.testTag("top_bar_menu_button")
                  ) {
                    Icon(
                      imageVector = Icons.Default.Menu,
                      contentDescription = "Open navigation menu"
                    )
                  }
                }
              },
              title = {
                Text(
                  text = when (uiState.activeTab) {
                    0 -> strings.navInventory
                    1 -> strings.navCategories
                    2 -> strings.navLocations
                    3 -> strings.navShopping
                    4 -> strings.navAlerts
                    5 -> strings.navSettings
                    6 -> strings.navFamily
                    7 -> strings.navTempZones
                    8 -> strings.navAboutApp
                    else -> strings.appName
                  },
                  style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
              },
              colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
              )
            )
          },
          bottomBar = {
            NavigationBar(
              modifier = Modifier
                .windowInsetsPadding(WindowInsets.navigationBars)
                .testTag("bottom_navigation_bar")
            ) {
              val alertsCount = uiState.restockAlerts.size

              NavigationBarItem(
                icon = {
                  Icon(
                    if (uiState.activeTab == 0) Icons.Default.Kitchen else Icons.Outlined.Kitchen,
                    contentDescription = strings.tabInventory
                  )
                },
                label = { Text(strings.tabInventory) },
                selected = uiState.activeTab == 0,
                onClick = { viewModel.selectTab(0) },
                modifier = Modifier.testTag("nav_tab_inventory")
              )

              NavigationBarItem(
                icon = {
                  val pendingCount = uiState.shoppingItems.count { !it.isPurchased }
                  BadgedBox(
                    badge = {
                      if (pendingCount > 0) {
                        Badge { Text("$pendingCount") }
                      }
                    }
                  ) {
                    Icon(
                      if (uiState.activeTab == 3) Icons.Default.ShoppingCart else Icons.Outlined.ShoppingCart,
                      contentDescription = strings.tabShopping
                    )
                  }
                },
                label = { Text(strings.tabShopping) },
                selected = uiState.activeTab == 3,
                onClick = { viewModel.selectTab(3) },
                modifier = Modifier.testTag("nav_tab_shopping")
              )

              NavigationBarItem(
                icon = {
                  BadgedBox(
                    badge = {
                      if (alertsCount > 0) {
                        Badge(
                          containerColor = MaterialTheme.colorScheme.error,
                          contentColor = MaterialTheme.colorScheme.onError
                        ) {
                          Text("$alertsCount")
                        }
                      }
                    }
                  ) {
                    Icon(
                      if (uiState.activeTab == 4) Icons.Default.NotificationsActive else Icons.Outlined.Notifications,
                      contentDescription = strings.tabAlerts
                    )
                  }
                },
                label = { Text(strings.tabAlerts) },
                selected = uiState.activeTab == 4,
                onClick = { viewModel.selectTab(4) },
                modifier = Modifier.testTag("nav_tab_alerts")
              )

              NavigationBarItem(
                icon = {
                  Icon(
                    if (uiState.activeTab == 8) Icons.Default.Info else Icons.Outlined.Info,
                    contentDescription = strings.tabAboutApp
                  )
                },
                label = { Text(strings.tabAboutApp) },
                selected = uiState.activeTab == 8,
                onClick = { viewModel.selectTab(8) },
                modifier = Modifier.testTag("nav_tab_about")
              )
            }
          }
        ) { innerPadding ->
          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(innerPadding)
          ) {
            when (uiState.activeTab) {
              0 -> InventoryScreen(uiState = uiState, viewModel = viewModel)
              1 -> CategoryManagementScreen(uiState = uiState, viewModel = viewModel)
              2 -> LocationManagementScreen(uiState = uiState, viewModel = viewModel)
              3 -> ShoppingListScreen(uiState = uiState, viewModel = viewModel)
              4 -> RestockAlertsScreen(uiState = uiState, viewModel = viewModel)
              5 -> SettingsScreen(uiState = uiState, viewModel = viewModel)
              6 -> FamilyMemberManagementScreen(uiState = uiState, viewModel = viewModel)
              7 -> TemperatureZoneManagementScreen(uiState = uiState, viewModel = viewModel)
              8 -> AboutAppScreen(uiState = uiState, viewModel = viewModel)
              else -> InventoryScreen(uiState = uiState, viewModel = viewModel)
            }
          }
        }
      }
    }
  }
}
