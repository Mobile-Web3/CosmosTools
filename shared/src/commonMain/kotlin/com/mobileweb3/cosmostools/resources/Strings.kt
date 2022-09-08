package com.mobileweb3.cosmostools.resources

object Strings {

    //bottom navigation
    const val VALIDATORS_SCREEN_TITLE = "Validators"
    const val STATS_SCREEN_TITLE = "Stats"
    const val TOOLS_SCREEN_TITLE = "Tools"
    const val WALLET_SCREEN_TITLE = "Wallet"

    //wallet screen
    const val EMPTY_WALLET_SCREEN_MESSAGE = "You don't have wallet yet!\n" +
            "Create new or import existing wallet\nby following easy steps.\n" +
            "We don't store your mnemonic phrases\nand private keys!"
    const val CREATE_WALLET_OPTION_TITLE = "Create wallet"
    const val RESTORE_WALLET_FROM_MNEMONIC_OPTION_TITLE = "Mnemonic"
    const val RESTORE_WALLET_FROM_PRIVATE_KEY_OPTION_TITLE = "Private key"
    const val SHARE_QR_OPTION = "Share QR"
    const val SHARE_TEXT_OPTION = "Share text"
    const val COPY_ADDRESS_OPTION = "Copy"
    const val SUCCESS_COPY_MESSAGE = "Address copied!"

    //select networks screen
    const val SELECT_NETWORKS_SCREEN_TITLE = "Select Networks"
    const val SELECT_NETWORKS_SCREEN_SEARCH_HINT = "Search network by title"
    const val SELECT_NETWORKS_SCREEN_UNSELECT_OPTION = "Unselect all"
    const val SELECT_NETWORKS_SCREEN_SELECT_OPTION = "Select all"

    //create-generate mnemonic screen
    const val MNEMONIC_EDITABLE_FIELD_HINT = "Mnemonic title"
    const val GENERATED_MNEMONIC_SCREEN_TITLE = "Generated Mnemonic"
    const val MNEMONIC_WARNING = "Warning! Losing your mnemonics could lead to loss of your assets. " +
            "We highly recommend keeping your mnemonics offline in a secure location. " +
            "Never share your mnemonics with anyone else!"
    const val COPY_MNEMONIC_OPTION = "Copy"
    const val SUCCESS_COPY_MNEMONIC = "Mnemonic copied!"
    const val DERIVE_WALLET_OPTION = "Derive Wallet"
    const val RESTORE_MNEMONIC_SCREEN_TITLE = "Enter Mnemonic"
    const val RESTORE_MNEMONIC_CLEAR_OPTION = "Clear"
    const val RESTORE_MNEMONIC_PASTE_OPTION = "Paste"

    //restore from private key screen
    const val RESTORE_PRIVATE_KEY_SCREEN_TITLE = "Enter Private Key"
    const val RESTORE_PRIVATE_KEY_SCREEN_EDITABLE_TITLE_HINT = "Private key title"
    const val RESTORE_PRIVATE_KEY_SCREEN_MESSAGE = "Please enter your private key.\n" +
            "The private key is a 66-digit string starting with 0x"
    const val RESTORE_PRIVATE_KEY_SCREEN_EDITABLE_HINT = "Insert Private key"
    const val RESTORE_PRIVATE_KEY_SCREEN_SCAN_OPTION = "Scan"
    const val RESTORE_PRIVATE_KEY_SCREEN_PASTE_OPTION = "Paste"
    const val RESTORE_PRIVATE_KEY_SCREEN_RESTORE_OPTION = "Restore"

    //derive screen
    const val SELECT_DERIVE_PATH_MESSAGE = "HD derivation path: "
    const val DERIVE_SCREEN_GENERATING_ADDRESSES_MESSAGE = "Generating addresses..."
    const val DERIVE_SCREEN_SAVE_ADDRESSES_OPTION = "Save"

    //delete wallet dialog
    const val DELETE_WALLET_DIALOG_TITLE = "Delete Wallet"
    const val DELETE_WALLET_DIALOG_MESSAGE_PT1 = "Do you want to delete address "
    const val DELETE_WALLET_DIALOG_MESSAGE_PT2 = " or delete all addresses created from "
    const val DELETE_WALLET_DIALOG_OPTION_ADDRESS = "Delete address"
    const val DELETE_WALLET_DIALOG_OPTION_SOURCE = "Delete "
    const val DELETE_WALLET_DIALOG_SUCCESS_ADDRESS_DELETE = "Address deleted!"
    const val DELETE_WALLET_DIALOG_SUCCESS_SOURCE_DELETE = " deleted!"

    //reveal source screen
    const val REVEAL_SOURCE_SCREEN_COPY_OPTION = "Copy"
    const val SUCCESS_REVEAL_SOURCE_SCREEN_COPY_OPTION = " copied!"
    const val REVEAL_SOURCE_SCREEN_DELETE_OPTION = "Delete"
    const val REVEAL_PRIVATE_KEY_MESSAGE = "Private key is only used to restore the currently selected wallet." +
            "We highly recommend keeping your mnemonics offline in a secure location, and never share your" +
            "mnemonics with anyone else.\n\n" +
            "Private key is NOT mnemonics." +
            "You MUST backup your mnemonics if you have one." +
            "You need your mnemonics to restore your original" +
            "wallets if you created them with mnemonics."

    //switch network wallet screen
    const val SWITCH_NETWORK_WALLET_SCREEN_TITLE = "Switch Network and Wallet"

}