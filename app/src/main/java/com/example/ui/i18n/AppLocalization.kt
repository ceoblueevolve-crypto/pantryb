package com.example.ui.i18n

/**
 * Localization strings and data translation dictionaries for:
 * - English (en)
 * - German (de)
 * - Italian (it)
 * - Portuguese (pt)
 */
data class ScreenStrings(
    // App Bar & Main Titles
    val appName: String,
    val navInventory: String,
    val navShopping: String,
    val navAlerts: String,
    val navSettings: String,
    val nav26Ai: String,
    val navSecurity: String,
    val navLanguage: String,

    // Bottom Bar Short Tab Labels
    val tabInventory: String,
    val tabCategories: String,
    val tabLocations: String,
    val tabShopping: String,
    val tabAlerts: String,
    val tabSettings: String,

    // Categories Screen & Management
    val navCategories: String,
    val categoriesTitle: String,
    val addCategory: String,
    val editCategory: String,
    val deleteCategory: String,
    val categoryName: String,
    val categoryIcon: String,
    val categoryColor: String,
    val categoryDescription: String,
    val defaultLocationLabel: String,
    val confirmDeleteCategory: String,
    val deleteCategoryWarning: String,
    val categoryCardBadge: String,
    val allCategoriesLabel: String,

    // Locations Screen & Management
    val navLocations: String,
    val locationsTitle: String,
    val addLocation: String,
    val editLocation: String,
    val deleteLocation: String,
    val locationName: String,
    val locationIcon: String,
    val locationColor: String,
    val locationDescription: String,
    val locationTemperatureZone: String,
    val confirmDeleteLocation: String,
    val deleteLocationWarning: String,
    val locationCardBadge: String,
    val allLocationsLabel: String,

    // Family Members Screen & Management
    val tabFamily: String,
    val navFamily: String,
    val familyTitle: String,
    val addFamilyMember: String,
    val editFamilyMember: String,
    val deleteFamilyMember: String,
    val memberName: String,
    val memberRole: String,
    val memberAvatar: String,
    val memberColor: String,
    val memberDietary: String,
    val memberLanguage: String,
    val memberDefaultProfile: String,
    val confirmDeleteMember: String,
    val deleteMemberWarning: String,
    val memberCardBadge: String,
    val allMembersLabel: String,
    val activeMemberBadge: String,
    val setActiveMember: String,

    // Drawer Labels
    val drawerHeaderTitle: String,
    val drawerHeaderSubtitle: String,
    val activeProfile: String,
    val mainNavigation: String,
    val settingsSubmenuTitle: String,
    val settingsSubmenuDesc: String,
    val lockApp: String,

    // Inventory Screen
    val inventoryHeroTitle: String,
    val itemsTracked: String,
    val alertsCount: String,
    val searchPlaceholder: String,
    val locationLabel: String,
    val noItemsFound: String,
    val tapToAdd: String,
    val threshold: String,
    val daysRemaining: String,
    val expiringSoon: String,
    val lowStock: String,
    val optimalStock: String,
    val inspectE2ee: String,
    val delete: String,
    val addItemTitle: String,
    val itemName: String,
    val category: String,
    val location: String,
    val quantity: String,
    val unit: String,
    val minThreshold: String,
    val barcode: String,
    val save: String,
    val cancel: String,

    // Shopping List Screen
    val shoppingHeroTitle: String,
    val toBuyTab: String,
    val purchasedTab: String,
    val clearPurchased: String,
    val shoppingEmpty: String,
    val shoppingEmptyPrompt: String,
    val requestedBy: String,
    val urgent: String,
    val markPurchased: String,
    val addShoppingItem: String,
    val notesPrompt: String,

    // Restock Alerts Screen
    val alertsHeroTitle: String,
    val activeAlertsBadge: String,
    val allGoodTitle: String,
    val allGoodSubtitle: String,
    val addToShoppingList: String,
    val dismiss: String,
    val suggested: String,
    val reasonLabel: String,

    // Settings Screen Tabs
    val tab26Ai: String,
    val tabSecurity: String,
    val tabLanguage: String,

    // Multi-Language Setup Screen
    val langSetupTitle: String,
    val langSetupSubtitle: String,
    val activeInterfaceLang: String,
    val autoDetectSystem: String,
    val autoDetectSynced: String,
    val autoDetectManual: String,
    val aiTranslationTitle: String,
    val aiTranslationDesc: String,
    val aiLiveDictionary: String,
    val householdMembersTitle: String,
    val householdMembersDesc: String,
    val selectAppLang: String,
    val regionalUnitsTitle: String,
    val unitOfMeasure: String,
    val currencyIndicator: String,
    val close: String,

    // Release 1.0.1 Strings
    val summary: String,
    val create: String,
    val editItemTitle: String,
    val totalCategories: String,
    val totalLocations: String,
    val navBackup: String,
    val tabBackup: String,

    // Release 1.1.2 Strings
    val myHomeInventory: String,
    val preferredLanguageCommunications: String,

    // Release 1.1.4 Strings - Temperature Zones
    val navTempZones: String,
    val tempZonesTitle: String,
    val addTempZone: String,
    val editTempZone: String,
    val deleteTempZone: String,
    val tempZoneName: String,
    val tempZoneIcon: String,
    val tempZoneColor: String,
    val tempZoneRange: String,
    val tempZoneDescription: String,
    val confirmDeleteTempZone: String,
    val deleteTempZoneWarning: String,
    val allTempZonesLabel: String,
    val totalTempZones: String,
    val searchTempZonesPlaceholder: String,
    val searchLocationsPlaceholder: String,
    val searchCategoriesPlaceholder: String,

    // Release 1.1.4 Strings - About App
    val navAboutApp: String,
    val tabAboutApp: String,
    val aboutAppTitle: String,
    val appVersion: String,
    val appDescription: String,
    val systemStatus: String,
    val developerPlatform: String,
    val databaseEngine: String,
    val encryptionDetails: String
)

object LocalizationManager {

    private val englishStrings = ScreenStrings(
        appName = "Pantry Inventory",
        navInventory = "House Inventory",
        navShopping = "Family Shopping List",
        navAlerts = "Restock Alerts",
        navSettings = "General Settings",
        nav26Ai = "Cloud Sync Configurations",
        navSecurity = "Security & Privacy",
        navLanguage = "Multi-Language Setup",

        tabInventory = "Inventory",
        tabCategories = "Categories",
        tabLocations = "Locations",
        tabShopping = "Shopping",
        tabAlerts = "Alerts",
        tabSettings = "Settings",

        navCategories = "Product Categories",
        categoriesTitle = "Category Management",
        addCategory = "Add Category",
        editCategory = "Edit Category",
        deleteCategory = "Delete Category",
        categoryName = "Category Name",
        categoryIcon = "Icon / Emoji",
        categoryColor = "Theme Color",
        categoryDescription = "Description",
        defaultLocationLabel = "Default Storage Location",
        confirmDeleteCategory = "Delete Category?",
        deleteCategoryWarning = "Items in this category will be moved to Uncategorized.",
        categoryCardBadge = "Active Category",
        allCategoriesLabel = "All Categories",

        navLocations = "Storage Locations",
        locationsTitle = "Storage Location Management",
        addLocation = "Add Location",
        editLocation = "Edit Location",
        deleteLocation = "Delete Location",
        locationName = "Location Name",
        locationIcon = "Icon / Emoji",
        locationColor = "Accent Color",
        locationDescription = "Description",
        locationTemperatureZone = "Storage / Temperature Zone",
        confirmDeleteLocation = "Delete Location?",
        deleteLocationWarning = "Items and categories in this location will be moved to No Specific Location.",
        locationCardBadge = "Active Location",
        allLocationsLabel = "All Locations",

        tabFamily = "Family",
        navFamily = "Family Members",
        familyTitle = "Summary",
        addFamilyMember = "Add Family Member",
        editFamilyMember = "Edit Family Member",
        deleteFamilyMember = "Delete Profile",
        memberName = "Member Name",
        memberRole = "Role / Relation",
        memberAvatar = "Avatar / Emoji",
        memberColor = "Profile Color",
        memberDietary = "Dietary Preferences & Allergies",
        memberLanguage = "Preferred Language",
        memberDefaultProfile = "Set as default active profile",
        confirmDeleteMember = "Are you sure you want to delete this family member?",
        deleteMemberWarning = "Shopping requests associated with this member will remain intact.",
        memberCardBadge = "Household Member",
        allMembersLabel = "All Members",
        activeMemberBadge = "Active Profile",
        setActiveMember = "Switch Profile",

        drawerHeaderTitle = "Pantry & Kitchen",
        drawerHeaderSubtitle = "Smart Inventory Manager",
        activeProfile = "Active Profile",
        mainNavigation = "MAIN NAVIGATION",
        settingsSubmenuTitle = "General Settings",
        settingsSubmenuDesc = "Cloud & Security Options",
        lockApp = "Lock Application",

        inventoryHeroTitle = "Household Pantry & Grocery",
        itemsTracked = "items tracked",
        alertsCount = "alerts",
        searchPlaceholder = "Search groceries, produce, pantry...",
        locationLabel = "Location:",
        noItemsFound = "No items found",
        tapToAdd = "Tap '+' to add household inventory items",
        threshold = "Threshold:",
        daysRemaining = "days left",
        expiringSoon = "Expiring soon",
        lowStock = "Low stock",
        optimalStock = "Optimal",
        inspectE2ee = "Inspect E2EE Ciphertext",
        delete = "Delete",
        addItemTitle = "Add Household Item",
        itemName = "Item Name",
        category = "Category",
        location = "Storage Location",
        quantity = "Quantity",
        unit = "Unit",
        minThreshold = "Min Alert Threshold",
        barcode = "Barcode (Optional)",
        save = "Save",
        cancel = "Cancel",

        shoppingHeroTitle = "Family Shopping List",
        toBuyTab = "To Buy",
        purchasedTab = "Purchased",
        clearPurchased = "Clear Purchased",
        shoppingEmpty = "Shopping list is clear",
        shoppingEmptyPrompt = "Add items for family members to pick up",
        requestedBy = "Requested by",
        urgent = "Urgent",
        markPurchased = "Purchased",
        addShoppingItem = "Add Shopping Item",
        notesPrompt = "Notes (e.g. brand, size)",

        alertsHeroTitle = "Predictive Restock Alerts",
        activeAlertsBadge = "Active Inventory Alerts",
        allGoodTitle = "Everything is stocked!",
        allGoodSubtitle = "No predictive run-outs or expirations detected by Oracle 26AI.",
        addToShoppingList = "Add to Shopping List",
        dismiss = "Dismiss",
        suggested = "Suggested:",
        reasonLabel = "Reason:",

        tab26Ai = "Cloud Sync",
        tabSecurity = "Security",
        tabLanguage = "Language",

        langSetupTitle = "Multi-Language Setup",
        langSetupSubtitle = "Household language personalization with Oracle 26AI real-time grocery translation.",
        activeInterfaceLang = "Active Interface Language",
        autoDetectSystem = "Auto-Detect Device Language",
        autoDetectSynced = "Synced with Android OS Locale",
        autoDetectManual = "Manual Language Override Enabled",
        aiTranslationTitle = "Oracle 26AI Cross-Language Translation",
        aiTranslationDesc = "Automatically translates grocery items, food categories, barcodes, and recipe notes across languages using Oracle 26AI Semantic Embeddings.",
        aiLiveDictionary = "AI Live Dictionary:",
        householdMembersTitle = "Household Member Language Setup",
        householdMembersDesc = "Assign different primary languages for each household profile.",
        selectAppLang = "SELECT APPLICATION LANGUAGE",
        regionalUnitsTitle = "Regional Formats & Units",
        unitOfMeasure = "Units of Measure",
        currencyIndicator = "Currency Indicator",
        close = "Close",

        summary = "Summary",
        create = "Create",
        editItemTitle = "Edit Item",
        totalCategories = "Total Categories",
        totalLocations = "Total Locations",
        navBackup = "Backup & Restore",
        tabBackup = "Backup",

        myHomeInventory = "My Home Inventory",
        preferredLanguageCommunications = "Pefered Language Communications",

        navTempZones = "Temperature Zones",
        tempZonesTitle = "Temperature Zones",
        addTempZone = "Add Temperature Zone",
        editTempZone = "Edit Temperature Zone",
        deleteTempZone = "Delete Temperature Zone",
        tempZoneName = "Zone Name",
        tempZoneIcon = "Icon / Emoji",
        tempZoneColor = "Accent Color",
        tempZoneRange = "Target Temperature",
        tempZoneDescription = "Description",
        confirmDeleteTempZone = "Delete Temperature Zone?",
        deleteTempZoneWarning = "Storage locations using this zone will keep their current settings.",
        allTempZonesLabel = "All Temperature Zones",
        totalTempZones = "Total Temperature Zones",
        searchTempZonesPlaceholder = "Search Temperature Zones...",
        searchLocationsPlaceholder = "Search Locations...",
        searchCategoriesPlaceholder = "Search Categories...",

        navAboutApp = "About App",
        tabAboutApp = "About",
        aboutAppTitle = "About Pantry Inventory",
        appVersion = "Version",
        appDescription = "Application Overview",
        systemStatus = "System Status & Integrity",
        developerPlatform = "Platform & Architecture",
        databaseEngine = "Local Persistence",
        encryptionDetails = "Security & Encryption"
    )

    private val germanStrings = ScreenStrings(
        appName = "Speisekammer Inventar",
        navInventory = "Hausinventar",
        navShopping = "Familien-Einkaufsliste",
        navAlerts = "Nachbestell-Warnungen",
        navSettings = "Allgemeine Einstellungen",
        nav26Ai = "Cloud-Sync-Konfigurationen",
        navSecurity = "Sicherheit & Datenschutz",
        navLanguage = "Mehrsprachige Einrichtung",

        tabInventory = "Inventar",
        tabCategories = "Kategorien",
        tabLocations = "Lagerorte",
        tabShopping = "Einkauf",
        tabAlerts = "Warnungen",
        tabSettings = "Einstellungen",

        navCategories = "Produktkategorien",
        categoriesTitle = "Kategorieverwaltung",
        addCategory = "Kategorie hinzufügen",
        editCategory = "Kategorie bearbeiten",
        deleteCategory = "Kategorie löschen",
        categoryName = "Kategoriename",
        categoryIcon = "Symbol / Emoji",
        categoryColor = "Themenfarbe",
        categoryDescription = "Beschreibung",
        defaultLocationLabel = "Standard-Lagerort",
        confirmDeleteCategory = "Kategorie löschen?",
        deleteCategoryWarning = "Artikel in dieser Kategorie werden auf 'Nicht kategorisiert' umgestellt.",
        categoryCardBadge = "Aktive Kategorie",
        allCategoriesLabel = "Alle Kategorien",

        navLocations = "Lagerorte & Zonen",
        locationsTitle = "Lagerort-Verwaltung",
        addLocation = "Lagerort hinzufügen",
        editLocation = "Lagerort bearbeiten",
        deleteLocation = "Lagerort löschen",
        locationName = "Lagerortname",
        locationIcon = "Symbol / Emoji",
        locationColor = "Akzentfarbe",
        locationDescription = "Beschreibung",
        locationTemperatureZone = "Temperatur- / Lagerzone",
        confirmDeleteLocation = "Lagerort löschen?",
        deleteLocationWarning = "Artikel und Kategorien an diesem Ort werden auf 'Kein bestimmter Ort' umgestellt.",
        locationCardBadge = "Aktiver Lagerort",
        allLocationsLabel = "Alle Lagerorte",

        tabFamily = "Familie",
        navFamily = "Familienmitglieder",
        familyTitle = "Übersicht",
        addFamilyMember = "Mitglied hinzufügen",
        editFamilyMember = "Mitglied bearbeiten",
        deleteFamilyMember = "Profil löschen",
        memberName = "Mitgliedsname",
        memberRole = "Rolle / Beziehung",
        memberAvatar = "Avatar / Emoji",
        memberColor = "Profilfarbe",
        memberDietary = "Ernährungspräferenzen & Allergien",
        memberLanguage = "Bevorzugte Sprache",
        memberDefaultProfile = "Als Standardprofil festlegen",
        confirmDeleteMember = "Möchten Sie dieses Familienmitglied wirklich löschen?",
        deleteMemberWarning = "Einkaufsanfragen dieses Mitglieds bleiben erhalten.",
        memberCardBadge = "Haushaltsmitglied",
        allMembersLabel = "Alle Mitglieder",
        activeMemberBadge = "Aktives Profil",
        setActiveMember = "Profil wechseln",

        drawerHeaderTitle = "Speisekammer & Küche",
        drawerHeaderSubtitle = "Intelligenter Inventar-Manager",
        activeProfile = "Aktives Profil",
        mainNavigation = "HAUPTMENÜ",
        settingsSubmenuTitle = "Allgemeine Einstellungen",
        settingsSubmenuDesc = "Cloud- & Sicherheitsoptionen",
        lockApp = "App sperren",

        inventoryHeroTitle = "Haushaltsvorrat & Lebensmittel",
        itemsTracked = "Artikel erfasst",
        alertsCount = "Warnungen",
        searchPlaceholder = "Lebensmittel, Obst, Vorrat suchen...",
        locationLabel = "Lagerort:",
        noItemsFound = "Keine Artikel gefunden",
        tapToAdd = "Tippe auf '+', um Artikel hinzuzufügen",
        threshold = "Mindestmenge:",
        daysRemaining = "Tage übrig",
        expiringSoon = "Läuft bald ab",
        lowStock = "Geringer Bestand",
        optimalStock = "Optimal",
        inspectE2ee = "E2EE-Geheimtext prüfen",
        delete = "Löschen",
        addItemTitle = "Artikel hinzufügen",
        itemName = "Artikelname",
        category = "Kategorie",
        location = "Lagerort",
        quantity = "Menge",
        unit = "Einheit",
        minThreshold = "Mindestwarnschwelle",
        barcode = "Barcode (Optional)",
        save = "Speichern",
        cancel = "Abbrechen",

        shoppingHeroTitle = "Familien-Einkaufsliste",
        toBuyTab = "Zu Kaufen",
        purchasedTab = "Gekauft",
        clearPurchased = "Gekaufte löschen",
        shoppingEmpty = "Einkaufsliste ist leer",
        shoppingEmptyPrompt = "Artikel hinzufügen, die die Familie mitbringen soll",
        requestedBy = "Angefordert von",
        urgent = "Dringend",
        markPurchased = "Gekauft",
        addShoppingItem = "Einkaufsartikel hinzufügen",
        notesPrompt = "Notizen (z. B. Marke, Größe)",

        alertsHeroTitle = "Vorausschauende Nachbestell-Warnungen",
        activeAlertsBadge = "Aktive Vorratswarnungen",
        allGoodTitle = "Alles bestens vorrätig!",
        allGoodSubtitle = "Keine bevorstehenden Engpässe oder Abläufe von Oracle 26AI erkannt.",
        addToShoppingList = "Zur Einkaufsliste hinzufügen",
        dismiss = "Verwerfen",
        suggested = "Empfohlen:",
        reasonLabel = "Grund:",

        tab26Ai = "Oracle 26AI",
        tabSecurity = "Sicherheit",
        tabLanguage = "Sprache",

        langSetupTitle = "Mehrsprachige Einrichtung",
        langSetupSubtitle = "Personalisierung der Haushaltssprache mit Oracle 26AI Echtzeit-Übersetzung.",
        activeInterfaceLang = "Aktive Oberflächensprache",
        autoDetectSystem = "Gerätesprache automatisch erkennen",
        autoDetectSynced = "Mit Android-Betriebssystem synchronisiert",
        autoDetectManual = "Manuelle Sprachauswahl aktiviert",
        aiTranslationTitle = "Oracle 26AI Mehrsprachige Übersetzung",
        aiTranslationDesc = "Übersetzt Lebensmittel, Kategorien, Barcodes und Notizen automatisch mit semantischen Oracle 26AI Embeddings.",
        aiLiveDictionary = "KI-Echtzeitwörterbuch:",
        householdMembersTitle = "Spracheinstellungen für Haushaltsmitglieder",
        householdMembersDesc = "Weise jedem Haushaltsprofil eine individuelle Hauptsprache zu.",
        selectAppLang = "ANWENDUNGSSPRACHE AUSWÄHLEN",
        regionalUnitsTitle = "Regionale Formate & Einheiten",
        unitOfMeasure = "Maßeinheiten",
        currencyIndicator = "Währungsanzeige",
        close = "Schließen",

        summary = "Übersicht",
        create = "Erstellen",
        editItemTitle = "Artikel bearbeiten",
        totalCategories = "Kategorien gesamt",
        totalLocations = "Lagerorte gesamt",
        navBackup = "Sichern & Wiederherstellen",
        tabBackup = "Sicherung",

        myHomeInventory = "Mein Haushaltsinventar",
        preferredLanguageCommunications = "Bevorzugte Kommunikationssprache",

        navTempZones = "Temperaturzonen",
        tempZonesTitle = "Temperaturzonen-Verwaltung",
        addTempZone = "Temperaturzone hinzufügen",
        editTempZone = "Temperaturzone bearbeiten",
        deleteTempZone = "Temperaturzone löschen",
        tempZoneName = "Zonenname",
        tempZoneIcon = "Symbol / Emoji",
        tempZoneColor = "Akzentfarbe",
        tempZoneRange = "Zieltemperatur",
        tempZoneDescription = "Beschreibung",
        confirmDeleteTempZone = "Temperaturzone löschen?",
        deleteTempZoneWarning = "Lagerorte, die diese Zone verwenden, behalten ihre aktuellen Einstellungen.",
        allTempZonesLabel = "Alle Temperaturzonen",
        totalTempZones = "Temperaturzonen gesamt",
        searchTempZonesPlaceholder = "Temperaturzonen suchen...",
        searchLocationsPlaceholder = "Lagerorte suchen...",
        searchCategoriesPlaceholder = "Kategorien suchen...",

        navAboutApp = "Über die App",
        tabAboutApp = "Über",
        aboutAppTitle = "Über Speisekammer Inventar",
        appVersion = "Version",
        appDescription = "Anwendungsübersicht",
        systemStatus = "Systemstatus & Integrität",
        developerPlatform = "Plattform & Architektur",
        databaseEngine = "Lokale Persistenz",
        encryptionDetails = "Sicherheit & Verschlüsselung"
    )

    private val italianStrings = ScreenStrings(
        appName = "Inventario Dispensa",
        navInventory = "Inventario Casa",
        navShopping = "Lista della Spesa",
        navAlerts = "Avvisi di Rifornimento",
        navSettings = "Impostazioni Generali",
        nav26Ai = "Configurazioni Cloud Sync",
        navSecurity = "Sicurezza e Privacy",
        navLanguage = "Configurazione Multilingue",

        tabInventory = "Inventario",
        tabCategories = "Categorie",
        tabLocations = "Posizioni",
        tabShopping = "Spesa",
        tabAlerts = "Avvisi",
        tabSettings = "Impostazioni",

        navCategories = "Categorie di Prodotti",
        categoriesTitle = "Gestione Categorie",
        addCategory = "Aggiungi Categoria",
        editCategory = "Modifica Categoria",
        deleteCategory = "Elimina Categoria",
        categoryName = "Nome Categoria",
        categoryIcon = "Icona / Emoji",
        categoryColor = "Colore Tema",
        categoryDescription = "Descrizione",
        defaultLocationLabel = "Posizione Predefinita",
        confirmDeleteCategory = "Eliminare Categoria?",
        deleteCategoryWarning = "Gli articoli in questa categoria verranno spostati su 'Non Categorizzato'.",
        categoryCardBadge = "Categoria Attiva",
        allCategoriesLabel = "Tutte le Categorie",

        navLocations = "Posizioni di Conservazione",
        locationsTitle = "Gestione Posizioni",
        addLocation = "Aggiungi Posizione",
        editLocation = "Modifica Posizione",
        deleteLocation = "Elimina Posizione",
        locationName = "Nome Posizione",
        locationIcon = "Icona / Emoji",
        locationColor = "Colore Accento",
        locationDescription = "Descrizione",
        locationTemperatureZone = "Zona di Temperatura / Tipo",
        confirmDeleteLocation = "Eliminare Posizione?",
        deleteLocationWarning = "Gli articoli e le categorie in questa posizione verranno spostati su 'Nessuna Posizione Specifica'.",
        locationCardBadge = "Posizione Attiva",
        allLocationsLabel = "Tutte le Posizioni",

        tabFamily = "Famiglia",
        navFamily = "Membri Famiglia",
        familyTitle = "Riepilogo",
        addFamilyMember = "Aggiungi Membro",
        editFamilyMember = "Modifica Membro",
        deleteFamilyMember = "Elimina Profilo",
        memberName = "Nome Membro",
        memberRole = "Ruolo / Parentela",
        memberAvatar = "Avatar / Emoji",
        memberColor = "Colore Profilo",
        memberDietary = "Preferenze Alimentari e Allergie",
        memberLanguage = "Lingua Preferita",
        memberDefaultProfile = "Imposta come profilo attivo predefinito",
        confirmDeleteMember = "Sei sicuro di voler eliminare questo membro della famiglia?",
        deleteMemberWarning = "Le richieste di spesa associate a questo membro rimarranno intatte.",
        memberCardBadge = "Membro della Casa",
        allMembersLabel = "Tutti i Membri",
        activeMemberBadge = "Profilo Attivo",
        setActiveMember = "Cambia Profilo",

        drawerHeaderTitle = "Dispensa e Cucina",
        drawerHeaderSubtitle = "Gestione Intelligente Dispensa",
        activeProfile = "Profilo Attivo",
        mainNavigation = "NAVIGAZIONE PRINCIPALE",
        settingsSubmenuTitle = "Impostazioni Generali",
        settingsSubmenuDesc = "Opzioni Cloud e Sicurezza",
        lockApp = "Blocca Applicazione",

        inventoryHeroTitle = "Dispensa e Spesa Domestica",
        itemsTracked = "articoli monitorati",
        alertsCount = "avvisi",
        searchPlaceholder = "Cerca generi alimentari, prodotti, dispensa...",
        locationLabel = "Posizione:",
        noItemsFound = "Nessun articolo trovato",
        tapToAdd = "Tocca '+' per aggiungere articoli alla dispensa",
        threshold = "Soglia:",
        daysRemaining = "giorni rimasti",
        expiringSoon = "In scadenza",
        lowStock = "Scorta bassa",
        optimalStock = "Ottimale",
        inspectE2ee = "Ispeziona Testo Cifrato E2EE",
        delete = "Elimina",
        addItemTitle = "Aggiungi Articolo",
        itemName = "Nome Articolo",
        category = "Categoria",
        location = "Posizione di Conservazione",
        quantity = "Quantità",
        unit = "Unità",
        minThreshold = "Soglia Minima di Avviso",
        barcode = "Codice a Barre (Opzionale)",
        save = "Salva",
        cancel = "Annulla",

        shoppingHeroTitle = "Lista della Spesa Familiare",
        toBuyTab = "Da Acquistare",
        purchasedTab = "Acquistato",
        clearPurchased = "Rimuovi Acquistati",
        shoppingEmpty = "La lista della spesa è vuota",
        shoppingEmptyPrompt = "Aggiungi articoli da acquistare per i familiari",
        requestedBy = "Richiesto da",
        urgent = "Urgente",
        markPurchased = "Acquistato",
        addShoppingItem = "Aggiungi Articolo Spesa",
        notesPrompt = "Note (es. marca, formato)",

        alertsHeroTitle = "Avvisi Predittivi di Rifornimento",
        activeAlertsBadge = "Avvisi di Scorta Attivi",
        allGoodTitle = "Tutto è ben rifornito!",
        allGoodSubtitle = "Nessun esaurimento o scadenza imminente rilevato da Oracle 26AI.",
        addToShoppingList = "Aggiungi alla Lista Spesa",
        dismiss = "Ignora",
        suggested = "Consigliato:",
        reasonLabel = "Motivo:",

        tab26Ai = "Cloud Sync",
        tabSecurity = "Sicurezza",
        tabLanguage = "Lingua",

        langSetupTitle = "Configurazione Multilingue",
        langSetupSubtitle = "Personalizzazione della lingua domestica con traduzione in tempo reale Oracle 26AI.",
        activeInterfaceLang = "Lingua Interfaccia Attiva",
        autoDetectSystem = "Rilevamento Automatico Lingua Dispositivo",
        autoDetectSynced = "Sincronizzato con il Locale di Android",
        autoDetectManual = "Selezione Manuale della Lingua Attiva",
        aiTranslationTitle = "Traduzione Multilingue Oracle 26AI",
        aiTranslationDesc = "Traduce automaticamente alimenti, categorie, codici a barre e note con gli embedding semantici di Oracle 26AI.",
        aiLiveDictionary = "Dizionario AI in Tempo Reale:",
        householdMembersTitle = "Lingua Membri della Famiglia",
        householdMembersDesc = "Assegna lingue diverse a ciascun profilo familiare.",
        selectAppLang = "SELEZIONA LINGUA DELL'APPLICAZIONE",
        regionalUnitsTitle = "Formati Regionali e Unità",
        unitOfMeasure = "Unità di Misura",
        currencyIndicator = "Indicatore Valuta",
        close = "Chiudi",

        summary = "Riepilogo",
        create = "Crea",
        editItemTitle = "Modifica Articolo",
        totalCategories = "Categorie Totali",
        totalLocations = "Posizioni Totali",
        navBackup = "Backup e Ripristino",
        tabBackup = "Backup",

        myHomeInventory = "Il Mio Inventario Casa",
        preferredLanguageCommunications = "Lingua di Comunicazione Preferita",

        navTempZones = "Zone di Temperatura",
        tempZonesTitle = "Gestione Zone Temperatura",
        addTempZone = "Aggiungi Zona di Temperatura",
        editTempZone = "Modifica Zona di Temperatura",
        deleteTempZone = "Elimina Zona di Temperatura",
        tempZoneName = "Nome Zona",
        tempZoneIcon = "Icona / Emoji",
        tempZoneColor = "Colore Accento",
        tempZoneRange = "Temperatura di Riferimento",
        tempZoneDescription = "Descrizione",
        confirmDeleteTempZone = "Eliminare la Zona di Temperatura?",
        deleteTempZoneWarning = "Le posizioni che utilizzano questa zona manterranno le impostazioni correnti.",
        allTempZonesLabel = "Tutte le Zone di Temperatura",
        totalTempZones = "Zone di Temperatura Totali",
        searchTempZonesPlaceholder = "Cerca Zone di Temperatura...",
        searchLocationsPlaceholder = "Cerca Posizioni...",
        searchCategoriesPlaceholder = "Cerca Categorie...",

        navAboutApp = "Informazioni sull'App",
        tabAboutApp = "Info",
        aboutAppTitle = "Informazioni su Inventario Dispensa",
        appVersion = "Versione",
        appDescription = "Panoramica dell'Applicazione",
        systemStatus = "Stato del Sistema & Integrità",
        developerPlatform = "Piattaforma & Architettura",
        databaseEngine = "Persistenza Locale",
        encryptionDetails = "Sicurezza & Crittografia"
    )

    private val portugueseStrings = ScreenStrings(
        appName = "Inventário da Despensa",
        navInventory = "Estoque da Casa",
        navShopping = "Lista de Compras",
        navAlerts = "Alertas de Reposição",
        navSettings = "Configurações Gerais",
        nav26Ai = "Configurações de Sincronização em Nuvem",
        navSecurity = "Segurança e Privacidade",
        navLanguage = "Configuração Multilíngue",

        tabInventory = "Inventário",
        tabCategories = "Categorias",
        tabLocations = "Locais",
        tabShopping = "Compras",
        tabAlerts = "Alertas",
        tabSettings = "Configurações",

        navCategories = "Categorias de Produtos",
        categoriesTitle = "Gerenciamento de Categorias",
        addCategory = "Adicionar Categoria",
        editCategory = "Editar Categoria",
        deleteCategory = "Excluir Categoria",
        categoryName = "Nome da Categoria",
        categoryIcon = "Ícone / Emoji",
        categoryColor = "Cor do Tema",
        categoryDescription = "Descrição",
        defaultLocationLabel = "Local Padrão",
        confirmDeleteCategory = "Excluir Categoria?",
        deleteCategoryWarning = "Os itens nesta categoria serão movidos para 'Não Categorizado'.",
        categoryCardBadge = "Categoria Ativa",
        allCategoriesLabel = "Todas as Categorias",

        navLocations = "Locais de Armazenamento",
        locationsTitle = "Gerenciamento de Locais",
        addLocation = "Adicionar Local",
        editLocation = "Editar Local",
        deleteLocation = "Excluir Local",
        locationName = "Nome do Local",
        locationIcon = "Ícone / Emoji",
        locationColor = "Cor de Destaque",
        locationDescription = "Descrição",
        locationTemperatureZone = "Zona de Temperatura / Tipo",
        confirmDeleteLocation = "Excluir Local?",
        deleteLocationWarning = "Os itens e categorias neste local serão movidos para 'Nenhum Local Específico'.",
        locationCardBadge = "Local Ativo",
        allLocationsLabel = "Todos os Locais",

        tabFamily = "Família",
        navFamily = "Membros da Família",
        familyTitle = "Resumo",
        addFamilyMember = "Adicionar Membro",
        editFamilyMember = "Editar Membro",
        deleteFamilyMember = "Excluir Perfil",
        memberName = "Nome do Membro",
        memberRole = "Função / Relação",
        memberAvatar = "Avatar / Emoji",
        memberColor = "Cor do Perfil",
        memberDietary = "Preferências Alimentares e Alergias",
        memberLanguage = "Idioma Preferido",
        memberDefaultProfile = "Definir como perfil ativo padrão",
        confirmDeleteMember = "Tem certeza de que deseja excluir este membro da família?",
        deleteMemberWarning = "Os pedidos de compras associados a este membro permanecerão intactos.",
        memberCardBadge = "Membro da Casa",
        allMembersLabel = "Todos os Membros",
        activeMemberBadge = "Perfil Ativo",
        setActiveMember = "Trocar Perfil",

        drawerHeaderTitle = "Despensa & Cozinha",
        drawerHeaderSubtitle = "Gerenciador Inteligente de Despensa",
        activeProfile = "Perfil Ativo",
        mainNavigation = "NAVEGAÇÃO PRINCIPAL",
        settingsSubmenuTitle = "Configurações Gerais",
        settingsSubmenuDesc = "Opções de Nuvem e Segurança",
        lockApp = "Bloquear Aplicativo",

        inventoryHeroTitle = "Despensa e Compras Domésticas",
        itemsTracked = "itens monitorados",
        alertsCount = "alertas",
        searchPlaceholder = "Buscar alimentos, produtos, despensa...",
        locationLabel = "Local:",
        noItemsFound = "Nenhum item encontrado",
        tapToAdd = "Toque em '+' para adicionar itens à despensa",
        threshold = "Limite:",
        daysRemaining = "dias restantes",
        expiringSoon = "Vencendo em breve",
        lowStock = "Estoque baixo",
        optimalStock = "Ideal",
        inspectE2ee = "Inspecionar Texto Cifrado E2EE",
        delete = "Excluir",
        addItemTitle = "Adicionar Item à Despensa",
        itemName = "Nome do Item",
        category = "Categoria",
        location = "Local de Armazenamento",
        quantity = "Quantidade",
        unit = "Unidade",
        minThreshold = "Limite Mínimo de Alerta",
        barcode = "Código de Barras (Opcional)",
        save = "Salvar",
        cancel = "Cancelar",

        shoppingHeroTitle = "Lista de Compras da Família",
        toBuyTab = "Para Comprar",
        purchasedTab = "Comprado",
        clearPurchased = "Limpar Comprados",
        shoppingEmpty = "A lista de compras está vazia",
        shoppingEmptyPrompt = "Adicione itens para a família comprar",
        requestedBy = "Solicitado por",
        urgent = "Urgente",
        markPurchased = "Comprado",
        addShoppingItem = "Adicionar Item de Compra",
        notesPrompt = "Notas (ex: marca, tamanho)",

        alertsHeroTitle = "Alertas Preditivos de Reposição",
        activeAlertsBadge = "Alertas de Estoque Ativos",
        allGoodTitle = "Tudo devidamente abastecido!",
        allGoodSubtitle = "Nenhum esgotamento ou vencimento iminente detectado pela Oracle 26AI.",
        addToShoppingList = "Adicionar à Lista de Compras",
        dismiss = "Descartar",
        suggested = "Sugerido:",
        reasonLabel = "Motivo:",

        tab26Ai = "Cloud Sync",
        tabSecurity = "Segurança",
        tabLanguage = "Idioma",

        langSetupTitle = "Configuração Multilíngue",
        langSetupSubtitle = "Personalização do idioma da casa com tradução em tempo real da Oracle 26AI.",
        activeInterfaceLang = "Idioma Ativo da Interface",
        autoDetectSystem = "Detectar Idioma do Dispositivo Automaticamente",
        autoDetectSynced = "Sincronizado com o Idioma do Android",
        autoDetectManual = "Seleção Manual de Idioma Ativada",
        aiTranslationTitle = "Tradução Multilíngue Oracle 26AI",
        aiTranslationDesc = "Traduz automaticamente itens de mercado, categorias, códigos de barras e notas com os embeddings da Oracle 26AI.",
        aiLiveDictionary = "Dicionário de IA em Tempo Real:",
        householdMembersTitle = "Idiomas dos Membros da Família",
        householdMembersDesc = "Defina idiomas preferidos para cada perfil familiar.",
        selectAppLang = "SELECIONAR IDIOMA DO APLICATIVO",
        regionalUnitsTitle = "Formatos Regionais e Unidades",
        unitOfMeasure = "Unidades de Medida",
        currencyIndicator = "Indicador de Moeda",
        close = "Fechar",

        summary = "Resumo",
        create = "Criar",
        editItemTitle = "Editar Item",
        totalCategories = "Total de Categorias",
        totalLocations = "Total de Locais",
        navBackup = "Backup e Restauração",
        tabBackup = "Backup",

        myHomeInventory = "Meu Inventário Doméstico",
        preferredLanguageCommunications = "Idioma de Comunicação Preferido",

        navTempZones = "Zonas de Temperatura",
        tempZonesTitle = "Gestão de Zonas de Temperatura",
        addTempZone = "Adicionar Zona de Temperatura",
        editTempZone = "Editar Zona de Temperatura",
        deleteTempZone = "Excluir Zona de Temperatura",
        tempZoneName = "Nome da Zona",
        tempZoneIcon = "Símbolo / Emoji",
        tempZoneColor = "Cor de Destaque",
        tempZoneRange = "Temperatura Alvo",
        tempZoneDescription = "Descrição",
        confirmDeleteTempZone = "Excluir Zona de Temperatura?",
        deleteTempZoneWarning = "Os locais que utilizam esta zona manterão suas configurações atuais.",
        allTempZonesLabel = "Todas as Zonas de Temperatura",
        totalTempZones = "Total de Zonas de Temperatura",
        searchTempZonesPlaceholder = "Buscar Zonas de Temperatura...",
        searchLocationsPlaceholder = "Buscar Locais...",
        searchCategoriesPlaceholder = "Buscar Categorias...",

        navAboutApp = "Sobre o App",
        tabAboutApp = "Sobre",
        aboutAppTitle = "Sobre o Inventário da Despensa",
        appVersion = "Versão",
        appDescription = "Visão Geral do Aplicativo",
        systemStatus = "Status e Integridade do Sistema",
        developerPlatform = "Plataforma e Arquitetura",
        databaseEngine = "Persistência Local",
        encryptionDetails = "Segurança e Criptografia"
    )

    fun getStrings(langCode: String): ScreenStrings {
        return when (langCode.lowercase()) {
            "de" -> germanStrings
            "it" -> italianStrings
            "pt" -> portugueseStrings
            else -> englishStrings
        }
    }

    // ==========================================
    // DATA LOCALIZATION DICTIONARIES
    // ==========================================

    private val itemNameTranslations = mapOf(
        "Pasture-Raised Eggs" to mapOf(
            "de" to "Freilandeier (Bio)",
            "it" to "Uova da Allevamento all'Aperto",
            "pt" to "Ovos Caipiras Orgânicos"
        ),
        "Extra Virgin Olive Oil" to mapOf(
            "de" to "Natives Olivenöl Extra",
            "it" to "Olio Extra Vergine di Oliva",
            "pt" to "Azeite de Oliva Extra Virgem"
        ),
        "Baby Spinach (Organic)" to mapOf(
            "de" to "Bio-Babyspinat",
            "it" to "Spinacino Novello Biologico",
            "pt" to "Espinafre Baby Orgânico"
        ),
        "Greek Vanilla Yogurt" to mapOf(
            "de" to "Griechischer Vanillejoghurt",
            "it" to "Yogurt Greco alla Vaniglia",
            "pt" to "Iogurte Grego de Baunilha"
        ),
        "Hass Avocados" to mapOf(
            "de" to "Hass-Avocados",
            "it" to "Avocado Hass",
            "pt" to "Abacates Hass"
        ),
        "Whole Grain Rolled Oats" to mapOf(
            "de" to "Vollkorn-Haferflocken",
            "it" to "Fiocchi d'Avena Integrali",
            "pt" to "Aveia em Flocos Integral"
        ),
        "Ground Arabica Coffee" to mapOf(
            "de" to "Gemahlener Arabica-Kaffee",
            "it" to "Caffè Arabica Macinato",
            "pt" to "Café Arábica Moído"
        ),
        "Sourdough Bread" to mapOf(
            "de" to "Sauerteigbrot",
            "it" to "Pane a Lievitazione Naturale",
            "pt" to "Pão de Fermentação Natural"
        ),
        "Almond Milk (Unsweetened)" to mapOf(
            "de" to "Ungesüßte Mandelmilch",
            "it" to "Latte di Mandorla Senza Zucchero",
            "pt" to "Leite de Amêndoas Sem Açúcar"
        ),
        "Almond Milk" to mapOf(
            "de" to "Mandelmilch",
            "it" to "Latte di Mandorla",
            "pt" to "Leite de Amêndoas"
        ),
        "Organic Honey" to mapOf(
            "de" to "Bio-Blütenhonig",
            "it" to "Miele Biologico",
            "pt" to "Mel Orgânico"
        ),
        "Atlantic Salmon Fillets" to mapOf(
            "de" to "Atlantik-Lachsfilets",
            "it" to "Filetti di Salmone dell'Atlantico",
            "pt" to "Filés de Salmão do Atlântico"
        ),
        "Dark Chocolate 85%" to mapOf(
            "de" to "Dunkle Schokolade 85%",
            "it" to "Cioccolato Fondente 85%",
            "pt" to "Chocolate Amargo 85%"
        ),
        "Organic Whole Milk" to mapOf(
            "de" to "Bio-Vollmilch",
            "it" to "Latte Intero Biologico",
            "pt" to "Leite Integral Orgânico"
        ),
        "San Marzano Canned Tomatoes" to mapOf(
            "de" to "San-Marzano-Dosentomaten",
            "it" to "Pomodori Pelati San Marzano",
            "pt" to "Tomates Pelados San Marzano"
        ),
        "Dijon Mustard" to mapOf(
            "de" to "Dijon-Senf",
            "it" to "Senape di Digione",
            "pt" to "Mostarda Dijon"
        ),
        "Fresh Basil" to mapOf(
            "de" to "Frisches Basilikum",
            "it" to "Basilico Fresco",
            "pt" to "Manjericão Fresco"
        )
    )

    private val categoryTranslations = mapOf(
        "All" to mapOf("de" to "Alle", "it" to "Tutti", "pt" to "Todos"),
        "Produce" to mapOf("de" to "Obst & Gemüse", "it" to "Frutta e Verdura", "pt" to "Frutas e Legumes"),
        "Dairy & Eggs" to mapOf("de" to "Milch & Eier", "it" to "Latticini e Uova", "pt" to "Laticínios e Ovos"),
        "Pantry" to mapOf("de" to "Speisekammer", "it" to "Dispensa", "pt" to "Despensa"),
        "Meat & Seafood" to mapOf("de" to "Fleisch & Fisch", "it" to "Carne e Pesce", "pt" to "Carnes e Peixes"),
        "Beverages" to mapOf("de" to "Getränke", "it" to "Bevande", "pt" to "Bebidas"),
        "Bakery" to mapOf("de" to "Bäckerei", "it" to "Panetteria", "pt" to "Padaria"),
        "Snacks" to mapOf("de" to "Snacks", "it" to "Spuntini", "pt" to "Lanches"),
        "Frozen" to mapOf("de" to "Tiefkühlkost", "it" to "Surgelati", "pt" to "Congelados"),
        "Uncategorized" to mapOf("de" to "Nicht kategorisiert", "it" to "Non Categorizzato", "pt" to "Não Categorizado")
    )

    private val locationTranslations = mapOf(
        "All" to mapOf("de" to "Alle", "it" to "Tutte", "pt" to "Todos"),
        "Fridge" to mapOf("de" to "Kühlschrank", "it" to "Frigorifero", "pt" to "Geladeira"),
        "Pantry" to mapOf("de" to "Speisekammer", "it" to "Dispensa", "pt" to "Despensa"),
        "Freezer" to mapOf("de" to "Gefrierschrank", "it" to "Congelatore", "pt" to "Freezer"),
        "Spice Rack" to mapOf("de" to "Gewürzregal", "it" to "Portaspezie", "pt" to "Porta-Temperos"),
        "Countertop" to mapOf("de" to "Arbeitsplatte", "it" to "Piano di Lavoro", "pt" to "Bancada"),
        "Wine & Cellar" to mapOf("de" to "Weinkeller", "it" to "Cantina Vini", "pt" to "Adega & Porão"),
        "Basement" to mapOf("de" to "Keller", "it" to "Cantina", "pt" to "Porão"),
        "No Specific Location" to mapOf("de" to "Kein bestimmter Ort", "it" to "Nessuna Posizione Specifica", "pt" to "Nenhum Local Específico")
    )

    private val unitTranslations = mapOf(
        "units" to mapOf("de" to "Stk.", "it" to "pz.", "pt" to "un."),
        "packs" to mapOf("de" to "Pck.", "it" to "conf.", "pt" to "pct."),
        "bottles" to mapOf("de" to "Fl.", "it" to "bott.", "pt" to "garrafas"),
        "kg" to mapOf("de" to "kg", "it" to "kg", "pt" to "kg"),
        "liters" to mapOf("de" to "L", "it" to "L", "pt" to "L")
    )

    private val alertReasonTranslations = mapOf(
        "Stock below minimum threshold" to mapOf(
            "de" to "Bestand unter Mindestmenge",
            "it" to "Scorta inferiore alla soglia minima",
            "pt" to "Estoque abaixo do limite mínimo"
        ),
        "Predicted to run out in 2 days" to mapOf(
            "de" to "Voraussichtlich in 2 Tagen aufgebraucht",
            "it" to "Esaurimento previsto tra 2 giorni",
            "pt" to "Previsão de término em 2 dias"
        ),
        "Predicted to run out in 1 days" to mapOf(
            "de" to "Voraussichtlich in 1 Tag aufgebraucht",
            "it" to "Esaurimento previsto tra 1 giorno",
            "pt" to "Previsão de término em 1 dia"
        ),
        "Predicted to run out in 3 days" to mapOf(
            "de" to "Voraussichtlich in 3 Tagen aufgebraucht",
            "it" to "Esaurimento previsto tra 3 giorni",
            "pt" to "Previsão de término em 3 dias"
        ),
        "Expiring in 2 days" to mapOf(
            "de" to "Läuft in 2 Tagen ab (Dringend)",
            "it" to "In scadenza tra 2 giorni (Urgente)",
            "pt" to "Vencendo em 2 dias (Urgente)"
        ),
        "Expiring soon" to mapOf(
            "de" to "Läuft bald ab",
            "it" to "In scadenza a breve",
            "pt" to "Vencendo em breve"
        ),
        "Oracle 26AI auto-restock recommendation" to mapOf(
            "de" to "Oracle 26AI automatische Nachbestell-Empfehlung",
            "it" to "Raccomandazione di riordino automatico Oracle 26AI",
            "pt" to "Recomendação de reposição automática Oracle 26AI"
        )
    )

    fun localizeItemName(name: String, langCode: String): String {
        if (langCode == "en") return name
        return itemNameTranslations[name]?.get(langCode) ?: name
    }

    fun localizeCategory(category: String, langCode: String): String {
        if (langCode == "en") return category
        return categoryTranslations[category]?.get(langCode) ?: category
    }

    fun localizeLocation(location: String, langCode: String): String {
        if (langCode == "en") return location
        return locationTranslations[location]?.get(langCode) ?: location
    }

    fun localizeUnit(unit: String, langCode: String): String {
        if (langCode == "en") return unit
        return unitTranslations[unit]?.get(langCode) ?: unit
    }

    fun localizeAlertReason(reason: String, langCode: String): String {
        if (langCode == "en") return reason
        // Try exact match or partial match
        alertReasonTranslations[reason]?.get(langCode)?.let { return it }
        for ((key, translations) in alertReasonTranslations) {
            if (reason.contains(key, ignoreCase = true)) {
                return translations[langCode] ?: reason
            }
        }
        return reason
    }

    private val memberTranslations = mapOf(
        "Mom" to mapOf("de" to "Mama", "it" to "Mamma", "pt" to "Mãe"),
        "Dad" to mapOf("de" to "Papa", "it" to "Papà", "pt" to "Pai"),
        "Teenager" to mapOf("de" to "Jugendlicher", "it" to "Ragazzo", "pt" to "Adolescente"),
        "Kid" to mapOf("de" to "Kind", "it" to "Bambino", "pt" to "Criança")
    )

    fun localizeMember(member: String, langCode: String): String {
        if (langCode == "en") return member
        return memberTranslations[member]?.get(langCode) ?: member
    }
}
