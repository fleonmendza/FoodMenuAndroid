package com.eflm.foodmenu.provider

import com.eflm.foodmenu.model.Alimento

class AlimentoProvider {
    companion object{
        val alimentosList = listOf<Alimento>(
            Alimento(
                "https://i.blogs.es/cb58af/enchiladas-h/1366_2000.jpg",
                "enchiladas",
                50,
                "Tortillas de maíz" +
                        "Salsa verde (hecha a base de chiles verdes, tomatillos, cebolla y ajo)\n" +
                        "Relleno (puede incluir pollo desmenuzado, queso, hongos o frijoles refritos)\n" +
                        "Cebolla picada\n" +
                        "Crema agria o crema mexicana\n" +
                        "Queso rallado (como queso cheddar o queso fresco)\n" +
                        "Aguacate (opcional, para guarnición)\n" +
                        "Hojas de cilantro (para decorar y dar sabor)",
                "Las enchiladas son un plato tradicional de la gastronomía mexicana que consiste en tortillas de maíz rellenas de diversos ingredientes, generalmente proteínas como carne, pollo o queso, y que están bañadas en una salsa picante de chiles secos. Estas tortillas rellenas se enrollan y se colocan en un plato, cubriéndolas con la salsa de chile y se cocinan al horno o se sirven de inmediato. La preparación de las enchiladas puede variar según la región de México y las preferencias personales. Además del relleno de carne o queso, a menudo se acompañan con cebolla, crema agria, aguacate y queso rallado. También es común servirlas con arroz, frijoles y guarniciones frescas como lechuga y tomate."
            ),
            Alimento(
                "https://i.blogs.es/4d59b8/como-hacer-consome-de-pollo-con-garbanzos-receta-tradicional-de-la-abuela/450_1000.jpg",
                "Consomé",
                90,
                "borrego, arroz, garbanzos, carne",
                "Caldo de borrego"
            ),
            Alimento(
                "https://images.ctfassets.net/n7hs0hadu6ro/1O0Be1dObiQBm17GQJHLj8/3fde720730f0b3616ecf5a82b928e7f9/pizza-a-domicilio-cerca-de-mi.jpg?w=1920&h=1281&fl=progressive&q=50&fm=jpg",
                "Pizza Margarita",
                350,
                "Masa, tomate, mozzarella, albahaca",
                "Masa con tomate, mozzarella y albahaca"
            ),
            Alimento(
                "https://assets.unileversolutions.com/recipes-v2/165292.jpg",
                "Ensalada César",
                250,
                "Lechuga, pollo, croutones, aderezo César",
                "Ensalada con lechuga, pollo, croutones y aderezo César"
            ),
            Alimento(
                "https://cdn2.cocinadelirante.com/sites/default/files/images/2023/01/tacos-arabes-caseros-receta-poblana.jpg",
                "Taco de carne asada",
                180,
                "Carne asada, tortilla, cebolla, cilantro",
                "Tortilla con carne asada, cebolla y cilantro"
            ),
            Alimento(
                "https://i.blogs.es/a17c5d/como-hacer-sushi-maki-recetas-mexico/1366_2000.jpg",
                "Sushi de salmón",
                220,
                "Arroz, salmón, alga nori",
                "Arroz, salmón y alga nori enrollados"
            ),
            Alimento(
                "https://cdn7.kiwilimon.com/recetaimagen/23667/960x640/17028.jpg.jpg",
                "Hamburguesa clásica",
                450,
                "Carne de res, pan, lechuga, tomate, queso",
                "Carne de res con pan, lechuga, tomate y queso"
            ),
            Alimento(
                "https://imagesvc.meredithcorp.io/v3/mm/image?url=https%3A%2F%2Fstatic.onecms.io%2Fwp-content%2Fuploads%2Fsites%2F21%2F2016%2F08%2F17%2Fpasta-alfredo.jpg-2000.jpg&q=60",
                "Espagueti a la bolognesa",
                320,
                "Espagueti, salsa de carne, queso parmesano",
                "Espagueti con salsa de carne y queso parmesano"
            )
        )
    }
}