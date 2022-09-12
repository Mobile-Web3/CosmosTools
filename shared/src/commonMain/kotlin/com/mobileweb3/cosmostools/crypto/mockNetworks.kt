package com.mobileweb3.cosmostools.crypto

val mockNetworks = listOf(
    Network(
        chain_name = "cosmoshub",
        status = "Live",
        website = null,
        pretty_name = "Cosmos Hub",
        bech32_prefix = "cosmos",
        key_algos = listOf("secp256k1"),
        slip44 = 118,
        fees = NetworkFees(
            fee_tokens = listOf(
                NetworkToken(
                    denom = "uatom",
                    fixed_min_gas_price = 0.0
                )
            )
        ),
        logo_URIs = null,
        explorers = listOf(
            NetworkExplorer(
                kind = "mintscan",
                url = "https://www.mintscan.io/cosmos",
                tx_page = "https://www.mintscan.io/cosmos/txs/",
                account_page = "https://www.mintscan.io/cosmos/account/"
            )
        ),
        assets = listOf(
            NetworkAsset(
                description = "The native staking and governance token of the Cosmos Hub.",
                denom_units = listOf(
                    NetworkToken(
                        denom = "uatom",
                        exponent = 0
                    ),
                    NetworkToken(
                        denom = "atom",
                        exponent = 6
                    )
                ),
                base = "uatom",
                name = "Cosmos",
                display = "atom",
                symbol = "ATOM",
                logo_URIs = NetworkLogos(
                    png = "https://raw.githubusercontent.com/cosmos/chain-registry/master/cosmoshub/images/atom.png",
                    svg = "https://raw.githubusercontent.com/cosmos/chain-registry/master/cosmoshub/images/atom.svg"
                ),
                coingecko_id = "cosmos"
            )
        )
    ),
    Network(
        chain_name = "osmosis",
        status = "live",
        website = null,
        pretty_name = "Osmosis",
        bech32_prefix = "osmo",
        key_algos = listOf("secp256k1"),
        slip44 = 118,
        fees = NetworkFees(
            fee_tokens = listOf(
                NetworkToken(
                    denom = "uosmo",
                    fixed_min_gas_price = 0.0,
                    low_gas_price = 0.0,
                    average_gas_price = 0.025,
                    high_gas_price = 0.04
                )
            ),
        ),
        staking = NetworkStaking(
            staking_tokens = listOf(
                NetworkToken(
                    denom = "uosmo"
                )
            )
        ),
        logo_URIs = NetworkLogos(
            png = "https://raw.githubusercontent.com/cosmos/chain-registry/master/osmosis/images/osmosis-chain-logo.png"
        ),
        explorers = listOf(
            NetworkExplorer(
                kind = "mintscan",
                url = "https://www.mintscan.io/osmosis",
                tx_page = "https://www.mintscan.io/osmosis/txs/",
                account_page = "https://www.mintscan.io/osmosis/account/"
            )
        ),
        assets = listOf(
            NetworkAsset(
                description = "The native token of Osmosis",
                denom_units = listOf(
                    NetworkToken(
                        denom = "uosmo",
                        exponent = 0
                    ),
                    NetworkToken(
                        denom = "osmo",
                        exponent = 6
                    ),
                ),
                base = "uosmo",
                name = "Osmosis",
                display = "osmo",
                symbol = "OSMO",
                logo_URIs = NetworkLogos(
                    png = "https://raw.githubusercontent.com/cosmos/chain-registry/master/osmosis/images/osmo.png",
                    svg = "https://raw.githubusercontent.com/cosmos/chain-registry/master/osmosis/images/osmo.svg"
                ),
                coingecko_id = "osmosis"
            ),
            NetworkAsset(
                denom_units = listOf(
                    NetworkToken(
                        denom = "uion",
                        exponent = 0
                    ),
                    NetworkToken(
                        denom = "ion",
                        exponent = 6
                    )
                ),
                base = "uion",
                name = "Ion",
                display = "ion",
                symbol = "ION",
                logo_URIs = NetworkLogos(
                    png = "https://raw.githubusercontent.com/cosmos/chain-registry/master/osmosis/images/ion.png",
                    svg = "https://raw.githubusercontent.com/cosmos/chain-registry/master/osmosis/images/ion.svg"
                ),
                coingecko_id = "ion"
            )
        )
    )
)