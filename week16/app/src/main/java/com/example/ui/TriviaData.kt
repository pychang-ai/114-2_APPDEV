package com.example.ui

data class TriviaQuestion(
    val id: Int,
    val questionZh: String,
    val questionEn: String,
    val optionsZh: List<String>,
    val optionsEn: List<String>,
    val correctIndex: Int,
    val explanationZh: String,
    val explanationEn: String
)

object TriviaData {
    val questions = listOf(
        TriviaQuestion(
            id = 1,
            questionZh = "寬吻海豚在睡覺時，是如何進行呼吸與防範深海威脅的呢？",
            questionEn = "How do bottlenose dolphins manage breathing and watch for predators while sleeping?",
            optionsZh = listOf(
                "大腦兩半球分開運算、交替休息，且保持一隻眼睛張開",
                "左右腦同時沉睡，藉由自動黏在水草上防止飄走",
                "沉到千米深海底憋氣，依靠極厚的外皮與護甲抵禦危險",
                "向陸地哺乳動物一樣進入全身深度沉睡，並關閉呼吸氣孔"
            ),
            optionsEn = listOf(
                "Resting one brain hemisphere at a time while keeping one eye open",
                "Shutting down the whole brain and wrapping themselves in seaweed",
                "Sinking to the deep ocean bed, holding their breath for hours",
                "Entering deep full-body sleep like land animals with closed blowholes"
            ),
            correctIndex = 0,
            explanationZh = "海豚睡覺時會採取「單側腦半球睡眠」，大腦交替小憩，這能讓牠們保持意識定時游浮到水面呼吸，並張開一隻眼睛防備掠食者。",
            explanationEn = "Dolphins employ 'unihemispheric slow-wave sleep' where only one half of the brain sleeps at a time. This allows them to stay conscious to float up for air and detect sharks."
        ),
        TriviaQuestion(
            id = 2,
            questionZh = "如果小丑魚珊瑚礁社群裡的唯一主宰「雌魚」過世，接下來群落會發生什麼變化呢？",
            questionEn = "What happens in a clownfish community if the dominant breeding female dies?",
            optionsZh = listOf(
                "全體小丑魚宣告群落解散，各自流落尋找其他海葵寄居",
                "群落裡體型最大、最成熟的「雄魚」會在數周內在生理上改變性別成為雌魚",
                "群落中最小的幼魚直接靠抽籤繼承群落首領",
                "群體將終其一生保持全雄魚狀態，直到所有母魚自然進入"
            ),
            optionsEn = listOf(
                "They immediately leave the anemone to wander across the sea floor",
                "The largest and most dominant male undergoes a physical sex change to female",
                "The smallest juvenile instantly takes command through a lucky draw",
                "They remain an all-male colony forever until an external female visits them"
            ),
            correctIndex = 1,
            explanationZh = "小丑魚是神奇的「雌雄同體（Sequential Hermaphrodites）」，生來皆為雄性。當群落唯一的母魚過世，最大的支配公魚會發生性別轉換轉為母魚領導家園。",
            explanationEn = "Clownfish are sequential hermaphrodites born male. If the colony's breeding female dies, the largest male transitions biologically into a female to sustain the pack."
        ),
        TriviaQuestion(
            id = 3,
            questionZh = "綠蠵龜媽媽在沙灘上產下卵，孵化出海龜寶寶的「性別」，通常是由什麼決定的呢？",
            questionEn = "What determines the gender of green sea turtles hatching from eggs in the nest?",
            optionsZh = listOf(
                "周圍海浪與洋流流經沙灘的速度與波長",
                "母龜在產卵前所攝取的海草多寡與營養組成",
                "沙灘卵窩內由太陽照射所累積的孵化「環境溫度」",
                "完全隨機的基因染色體配對，不受任何外在環境干擾"
            ),
            optionsEn = listOf(
                "The velocity and wavelength of nearby ocean currents",
                "The quantity and nutritional components of algae eaten by the mother",
                "The environmental temperature of the sandy nest heated by sunlight",
                "Completely random genetic matches independent of external ecosystems"
            ),
            correctIndex = 2,
            explanationZh = "海龜的性別取決於「卵窩溫度」。一般而言，孵化溫度高於臨界值（通常約 29.7°C）會孵化出母龜，低於此溫度則孵化出公龜，這就是著名的「溫度性別決定機制」。",
            explanationEn = "Green sea turtles exhibit 'Temperature-dependent sex determination' (TSD). Warmer sand inside the nest produces females ('hot chicks'), while cooler sand produces males ('cool dudes')."
        ),
        TriviaQuestion(
            id = 4,
            questionZh = "一隻北太平洋巨型章魚，體內一共有幾個「心臟」，以及牠的血液呈什麼光澤？",
            questionEn = "How many hearts does a Giant Pacific Octopus have, and what color is its blood?",
            optionsZh = listOf(
                "一個普通心臟，流動著亮橘色多氧血液",
                "兩個心臟，流動著完全透明如同海水的血液",
                "三個心臟，流動著富含銅、呈高貴神秘的「藍色」血液",
                "沒有常規心臟，流動著淡黃色淋巴細胞液"
            ),
            optionsEn = listOf(
                "One simple heart with flowing bright orange blood",
                "Two hearts with fully transparent fluid resembling seawater",
                "Three hearts with blue blood enriched by oxygen-carrying copper proteins",
                "No physical hearts with light yellow lymphatic fluid"
            ),
            correctIndex = 2,
            explanationZh = "章魚擁有三個心臟：兩個腮心臟專門泵血入呼吸腮，一個體心臟推進血液到全身器官。其血液呈藍色，是因為利用「血藍蛋白（Hemocyanin）」進行氧氣載體。",
            explanationEn = "These amazing cephalopods have three hearts (two gills hearts, one systemic heart). Their blood is blue because they use copper-based hemocyanin instead of iron-based hemoglobin."
        ),
        TriviaQuestion(
            id = 5,
            questionZh = "體重重達上百噸、堪稱地球最龐大物種的「藍鯨」，主要靠捕食什麼食物維生？",
            questionEn = "How does the colossal blue whale (the largest animal on Earth) sustain its massive weight?",
            optionsZh = listOf(
                "在深海瘋狂追逐大白鯊與巨型大王酸漿魷",
                "富含礦物質且能行光合作用的海草與珊瑚芽孢",
                "體型只有幾公分、小到驚人的「磷蝦（Krill）」與微小浮游生物",
                "吞噬沉降在海底的死魚屍骸或鯨落生物群落"
            ),
            optionsEn = listOf(
                "Hunting down sharks and giant deep-sea giant squids",
                " Grazing on thick undersea kelp beds and coral spores",
                "Filtering tiny krill (often just centimeters long) and microscopic plankton",
                "Clearing biological leftovers or sunken organic mass on the ocean floor"
            ),
            correctIndex = 2,
            explanationZh = "藍鯨雖然體型巨大（甚至比恐龍還巨型），但食物幾乎完全是長度僅數公分的微小「磷蝦」。牠們一張大口，用特化的角質鯨鬚篩板將海水一口氣吐出，並留下數百萬隻高營養磷蝦吞食。",
            explanationEn = "Despite weighing up to 150 tons, blue whales feed almost exclusively on microscopic krill. They use their specialized baleen plates like a sieve to trap up to 4 tons of tiny shrimp-like krill daily."
        )
    )
}
