package com.example.onlishop.network.items

import com.example.onlishop.R
import com.example.onlishop.app.App
import com.example.onlishop.database.models.ShopGroup
import com.example.onlishop.database.models.ShopItem
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class ItemsServiceImpl: ItemsService {

    override fun getItems(): List<ShopItem> {
        return getMockList()
    }

    override fun getItemsRx(): Single<List<ShopItem>> {
        App.logger.logDevWithThread("getItemsRx service")
        return Single.just(getMockList())
    }

    private fun getMockList(): List<ShopItem> {
        return listOf(
            ShopItem(
                id = 1,
                groupId = 11,
                name = "АНОРАК ЧЕРНЫЙ",
                description = "О товаре\n" +
                        "S - длина анорака от ворота до низа -82 см, от плеча до плеча - 52 см, длина рукава от плеча - 73 см. M - длина анорака от ворота до низа - 82 см, от плеча до плеча - 52 см, длина рукава от плеча - 74 см. L - длина анорака от ворота до низа - 82 см, от плеча до плеча - 54 см, длина рукава от плеча - 74 см. XL - длина анорака от ворота до низа - 82 см, от плеча до плеча - 56 см, длина рукава от плеча - 76 см.",
                imageDrawable = R.drawable.man_top_1,
                price = 2200,
                sizes = "s,m,l,xl"
            ),
            ShopItem(
                id = 2,
                groupId = 11,
                name = "ЖИЛЕТКА \"G\"",
                description = "Ткань плотная , приятная на ощупь.\n" +
                        "\n" +
                        "Это лимитированный выпуск !\n" +
                        "\n" +
                        "Буква G на всю спину выполнена из основной ткани \n" +
                        "\n" +
                        "Над посадкой работали очень долго и довели ее до идеала !",
                imageDrawable = R.drawable.man_top_2,
                price = 2850,
                sizes = "m,l"
            ),
            ShopItem(
                id = 3,
                groupId = 11,
                name = "\"ДОЖДЕВИК\" С ЧЕРНЫМ КАНТОМ",
                description = "О товаре\n" +
                        "Дождевик с черным кантом S от плеча до плеча 42 ОГ 112 Длина по спинке 81 M от плеча до плеча 44 ОГ 118 Длина по спинке 82 L от плеча до плеча 47 ОГ 124 Длина по спинке 83 XL от плеча до плеча 50 ОГ 130 Длина по спинке 84",
                imageDrawable = R.drawable.man_top_3,
                price = 4050,
                sizes = "m,l"
            ),
            ShopItem(
                id = 4,
                groupId = 12,
                name = "ШТАНЫ \"LIMITED\"",
                description = "- Эластичный пояс\n" +
                        "- Эластичные манжеты по низу.\n" +
                        "- Белые и неоново-желтые принты на передней и задней части\n" +
                        "- Два боковых кармана\n" +
                        "- Унисекс\n" +
                        "100% хлопок",
                imageDrawable = R.drawable.man_bot_1,
                price = 2550,
                sizes = "s,m,l,xl,xxl"
            ),
            ShopItem(
                id = 5,
                groupId = 12,
                name = "ШОРТЫ OG LOGO",
                description = "Шорты из базовой коллекции OG",
                imageDrawable = R.drawable.man_bot_2,
                price = 1490,
                sizes = "s,m,l,xl"
            ),
            ShopItem(
                id = 6,
                groupId = 12,
                name = "ШТАНЫ СЕРЫЕ",
                description = "О товаре\n" +
                        "S - Длина -100, Нижняя манжета - 13, ОБ - 120.\n" +
                        "M - Длина - 100, Нижняя манжета - 13,5. ОБ - 126.\n" +
                        "L - Длина - 100, Нижняя манжета - 14, ОБ - 136.\n",
                imageDrawable = R.drawable.man_bot_3,
                price = 1580,
                sizes = "s,m,l,xl"
            ),
            ShopItem(
                id = 7,
                groupId = 13,
                name = "T-SHIRT “STFU” BLACK",
                description = "S\n" +
                        "ДЛИНА 75\n" +
                        "ОБЪЁМ 114\n" +
                        "РУКАВ 38\n" +
                        "\n" +
                        "M\n" +
                        "ДЛИНА 77\n" +
                        "ОБЪЁМ 116\n" +
                        "РУКАВ 40\n" +
                        "L\n" +
                        "ДЛИНА 78\n" +
                        "ОБЪЁМ 118\n" +
                        "РУКАВ 41\n" +
                        "XL\n" +
                        "ДЛИНА 80\n" +
                        "ОБЪЁМ 120\n" +
                        "РУКАВ 43",
                imageDrawable = R.drawable.man_tshirt_1,
                price = 1450,
                sizes = "s,m,l,xl"
            ),
            ShopItem(
                id = 8,
                groupId = 13,
                name = "T-SHIRT \"STFU\" WHITE",
                description = "S\n" +
                        "ДЛИНА 75\n" +
                        "ОБЪЁМ 114\n" +
                        "РУКАВ 38\n" +
                        "\n" +
                        "M\n" +
                        "ДЛИНА 77\n" +
                        "ОБЪЁМ 116\n" +
                        "РУКАВ 40\n" +
                        "\n" +
                        "L\n" +
                        "ДЛИНА 78\n" +
                        "ОБЪЁМ 118\n" +
                        "РУКАВ 41\n" +
                        "\n" +
                        "XL\n" +
                        "ДЛИНА 80\n" +
                        "ОБЪЁМ 120\n" +
                        "РУКАВ 43\n",
                imageDrawable = R.drawable.man_tshirt_2,
                price = 1450,
                sizes = "s,m,l,xl"
            ),
            ShopItem(
                id = 9,
                groupId = 13,
                name = "T-SHIRT \"NAS\"",
                description = "S\n" +
                        "ДЛИНА 75\n" +
                        "ОБЪЁМ 114\n" +
                        "РУКАВ 38\n" +
                        "\n" +
                        "M\n" +
                        "ДЛИНА 77\n" +
                        "ОБЪЁМ 116\n" +
                        "РУКАВ 40\n" +
                        "\n" +
                        "L\n" +
                        "ДЛИНА 78\n" +
                        "ОБЪЁМ 118\n" +
                        "РУКАВ 41\n" +
                        "\n" +
                        "XL\n" +
                        "ДЛИНА 80\n" +
                        "ОБЪЁМ 120\n" +
                        "РУКАВ 43",
                imageDrawable = R.drawable.man_tshirt_3,
                price = 1600,
                sizes = "s,m,l,xl"
            ),
            ShopItem(
                id = 10,
                groupId = 21,
                name = "HOODIE OG \"FLAMINGO\"",
                description = "",
                imageDrawable = R.drawable.woman_top_1,
                price = 2500,
                sizes = "s,m,l,xl"
            ),
            ShopItem(
                id = 11,
                groupId = 21,
                name = "HOODIE \"METROPOLITAN\"",
                description = "",
                imageDrawable = R.drawable.woman_top_2,
                price = 2500,
                sizes = "s,m,l,xl"
            ),
            ShopItem(
                id = 12,
                groupId = 21,
                name = "РУБАШКА \"LEGEND\"",
                description = "90%-Хлопок 10%-Эластан",
                imageDrawable = R.drawable.woman_top_3,
                price = 3000,
                sizes = "s,m,l,xl"
            ),
            ShopItem(
                id = 13,
                groupId = 22,
                name = "ШТАНЫ СЕРЫЕ \"LOGO\"",
                description = "О товаре\n" +
                        "S Длина 95, Резинка 30 см, ОБ 96см;\n" +
                        "M Длина 95, Резинка 33 см, ОБ 104см;\n" +
                        "L Длина 95, Резинка 36 см, ОБ 110см;\n",
                imageDrawable = R.drawable.woman_bot_1,
                price = 1450,
                sizes = "s,m,l"
            ),
            ShopItem(
                id = 14,
                groupId = 22,
                name = "ДЖОГГЕРЫ \"LOGO\"",
                description = "О товаре:\n" +
                        "Размер S: Бёдра: 105 см Талия: 68 см Длина: 90 см\n" +
                        "Размер: М Бёдра: 108 см Талия: 74 см Длина: 93 см\n" +
                        "Размер: L Бёдра: 110 см Талия: 78 см Длина: 100 см",
                imageDrawable = R.drawable.woman_bot_2,
                price = 1500,
                sizes = "s,m,l"
            ),
            ShopItem(
                id = 15,
                groupId = 22,
                name = "ШТАНЫ \"LEGEND\"",
                description = "90%-Хлопок 10%-Эластан",
                imageDrawable = R.drawable.woman_bot_3,
                price = 2750,
                sizes = "m"
            ),
            ShopItem(
                id = 16,
                groupId = 23,
                name = "T-shirt “STFU” WHITE",
                description = "S\n" +
                        "ДЛИНА 75\n" +
                        "ОБЪЁМ 114\n" +
                        "РУКАВ 38\n" +
                        "\n" +
                        "\n" +
                        "M\n" +
                        "ДЛИНА 77\n" +
                        "ОБЪЁМ 116\n" +
                        "РУКАВ 40\n" +
                        "\n" +
                        "L\n" +
                        "ДЛИНА 78\n" +
                        "ОБЪЁМ 118\n" +
                        "РУКАВ 41\n" +
                        "\n" +
                        "XL\n" +
                        "ДЛИНА 80\n" +
                        "ОБЪЁМ 120\n" +
                        "РУКАВ 43",
                imageDrawable = R.drawable.woman_tshirt_1,
                price = 1470,
                sizes = "s,m,l,xl"
            ),
            ShopItem(
                id = 17,
                groupId = 23,
                name = "\"LIMITED\" NEON PINK",
                description = "Желто-белые шелкотрафаретные надписи спереди и сзади\n" +
                        "Ребристый Crewneck\n" +
                        "\n" +
                        "100% хлопок\n" +
                        "\n" +
                        "Это эксклюзивная ограниченная серия, доступная только в магазинах Taboo и Luisaviaroma.",
                imageDrawable = R.drawable.woman_tshirt_2,
                price = 1700,
                sizes = "s,m,l,xl,xxl"
            ),
            ShopItem(
                id = 18,
                groupId = 23,
                name = "\"LIMITED\" NEON YELLOW",
                description = "- Черно-белые шелкотрафаретные надписи спереди и сзади\n" +
                        "- Ребристый Crewneck\n" +
                        "\n" +
                        "100% хлопок\n" +
                        "\n" +
                        "Это эксклюзивная ограниченная серия, доступная только в магазинах Taboo и Luisaviaroma.",
                imageDrawable = R.drawable.woman_tshirt_3,
                price = 1700,
                sizes = "s,m,l,xl,xxl"
            ),
        )
    }

}