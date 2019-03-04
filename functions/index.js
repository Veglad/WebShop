const functions = require('firebase-functions');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
exports.products = functions.https.onRequest((request, response) => {
    var devices = [                   {
                      "name" : "iPhone X",
                      "price" : 1149,
                        "shortDescription" : "Apple iPhone",
                        "longDescription" : "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                        "imageUrl" : "https://support.apple.com/library/APPLE/APPLECARE_ALLGEOS/SP770/iphonex.png",
                        "inStok" : 4,
                        "isPopular" : true,
                        "bought" : 40,
                        "promotional" : 0,
                        "imageThumbnailUrl" : "https://cdn2.gsmarena.com/vv/bigpic/apple-iphone-x.jpg"
                    },
                    {
                        "name" : "Pixel",
                        "price" : 499,
                        "shortDescription" : "Google Pixel",
                        "longDescription" : "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                        "imageUrl" : "https://cdn.shopify.com/s/files/1/1352/5175/products/Ringke_Google_Pixel2_XL_Fusion_main_thum_Clear_1500_1400x.jpg?v=1509455415",
                        "inStok" : 2,
                        "isPopular" : false,
                        "bought" : 1,
                        "promotional" : 25,
                        "imageThumbnailUrl" : "https://cdn.shopify.com/s/files/1/1352/5175/products/Ringke_Google_Pixel2_XL_Fusion_main_thum_Clear_1500_1400x.jpg?v=1509455415"
                    },
                    {
                        "name" : "Apple Watch Series 3",
                        "price" : 399,
                        "shortDescription" : "Apple Watch",
                        "longDescription" : "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                            "imageUrl" : "https://www.bhphotovideo.com/images/images2500x2500/apple_mqk62ll_a_watch_series_3_42mm_1362215.jpg",
                        "inStok" : 8,
                        "isPopular" : true,
                        "bought" : 37,
                        "promotional" : 0,
                        "imageThumbnailUrl" : "https://smartzoz.com/wp-content/uploads/2018/01/apple-watch-sport-42mm2-160x212.jpg"
                    },
                    {
                        "name" : "Samsung Galaxy S9",
                        "price" : 719,
                        "shortDescription" : "Samsung",
                        "longDescription" : "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                        "imageUrl" : "https://cdn2.gsmarena.com/vv/pics/samsung/samsung-galaxy-s9-1.jpg",
                        "inStok" : 4,
                        "isPopular" : true,
                        "bought" : 12,
                        "promotional" : 1,
                        "imageThumbnailUrl" :  "https://smartzoz.com/wp-content/uploads/2018/01/samsung-galaxy-s8--160x212.jpg"
                    },
                    {
                        "name" : "iPad Pro 12.9",
                        "price" : 799,
                        "shortDescription" : "Apple iPad",
                        "longDescription" : "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                            "imageUrl" : "https://assets.pcmag.com/media/images/549842-apple-ipad-pro-12-9-inch-2017.jpg?thumb:y&width:740&height:416",
                        "inStok" : 5,
                        "isPopular" : true,
                        "bought" : 24,
                        "promotional" : 0,
                        "imageThumbnailUrl" : "https://i0.wp.com/techenroll.com/wp-content/uploads/2017/03/DTz9w1489222672-160x212.jpg"
                    },
                    {
                        "name" : "Acer Predator",
                        "price" : 8999,
                        "shortDescription" : "Gaming laptop form Acer",
                        "longDescription" : "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                          "imageUrl" : "https://assets.pcmag.com/media/images/413583-acer-predator-15.jpg?width:740&height:416",
                        "inStok" : 5,
                        "isPopular" : true,
                        "bought" : 20,
                        "promotional" : 18,
                        "imageThumbnailUrl" : "https://images-na.ssl-images-amazon.com/images/G/01/aplusautomation/vendorimages/d68b60f4-40f1-4d8d-94d3-93b97fb41567.jpg._CB278436535__SR285,285_.jpg"
                    },
                    {
                        "name" : "OnePlus 6",
                        "price" : 529,
                        "shortDescription" : "OnePlus",
                        "longDescription" : "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                          "imageUrl" : "https://cdn2.gsmarena.com/vv/pics/oneplus/oneplus-6-5.jpg",
                        "inStok" : 4,
                        "isPopular" : true,
                        "bought" : 14,
                        "promotional" : 0,
                        "imageThumbnailUrl" : "https://smartzoz.com/wp-content/uploads/2018/05/oneplus-6--160x212.jpg"
                    },
                    {
                        "name" : "Playstation 4 Pro",
                        "price" : 399,
                        "shortDescription" : "Sony Playstation",
                        "longDescription" : "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                         "imageUrl" : "https://media.wired.com/photos/5a99f809b4bf6c3e4d405abc/master/pass/PS4-Pro-SOURCE-Sony.jpg",
                        "inStok" : 8,
                        "isPopular" : true,
                        "bought" : 34,
                        "promotional" : 48,
                        "imageThumbnailUrl" : "https://rukminim1.flixcart.com/image/312/312/je4k5u80/gamingconsole/h/r/g/1-ps4-pro-sony-na-original-imaf2vzcrrxnhps9.jpeg?q:70"
                    },
                    {
                        "name" : "Samsung Gear S3",
                        "price" : 349,
                        "shortDescription" : "Samsung watch",
                        "longDescription" : "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                        "imageUrl" : "http://www.samsung.com/global/galaxy/gear-s3/images/gear-s3_highlights_kv_ft_l.jpg",
                        "inStok" : 7,
                        "isPopular" : true,
                        "bought" : 23,
                        "promotional" : 10,
                        "imageThumbnailUrl" : "https://smartzoz.com/wp-content/uploads/2018/01/samsung-gear-sport1-160x212.jpg"
                    }
]
  response.send(JSON.stringify(devices));
});
