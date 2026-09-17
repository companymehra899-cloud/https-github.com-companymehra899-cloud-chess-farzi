package com.example.chess.model

import androidx.compose.ui.graphics.Color

enum class AvatarCategory(val title: String) {
    MALE("Boys 3D"),
    FEMALE("Girls 3D"),
    ANIMAL("Animals 3D"),
    BIRD("Birds 3D"),
    ROBOT("Robots 3D")
}

data class AvatarItem(
    val id: String,
    val name: String,
    val category: AvatarCategory,
    val iconKey: String, // identifies the vector illustration
    val primaryColor: Long,
    val secondaryColor: Long,
    val imageUrl: String = "",
    val drawableResId: Int? = null
)

data class AvatarBorder(
    val id: String,
    val name: String,
    val color: Color
)

object AvatarRepository {
    val BORDERS = listOf(
        AvatarBorder("gold", "Royal Gold", Color(0xFFFFD700)),
        AvatarBorder("cyan", "Neon Cyan", Color(0xFF00E5FF)),
        AvatarBorder("emerald", "Emerald Green", Color(0xFF00E676)),
        AvatarBorder("crimson", "Crimson Red", Color(0xFFFF1744)),
        AvatarBorder("purple", "Imperial Violet", Color(0xFFD500F9)),
        AvatarBorder("amber", "Warm Amber", Color(0xFFFF9100))
    )

    val TITLES = listOf(
        "Grandmaster",
        "Tactician",
        "Knight Rider",
        "Rook Whisperer",
        "Speed Demon",
        "Rising Star",
        "Chess Wizard",
        "Guest Master"
    )

    val ALL_AVATARS: List<AvatarItem> = listOf(
        // 3D Cartoon Boys & Masters (Global & Cultural Styles)
        AvatarItem("m_1", "3D Boy Master", AvatarCategory.MALE, "male_king", 0xFF1E3A8A, 0xFF3B82F6, drawableResId = com.example.R.drawable.img_avatar_boy),
        AvatarItem("m_2", "Tactician Arthur", AvatarCategory.MALE, "male_tactician", 0xFF312E81, 0xFF6366F1),
        AvatarItem("m_3", "Prodigy Alex", AvatarCategory.MALE, "male_young", 0xFF065F46, 0xFF10B981),
        AvatarItem("m_4", "Cyber Victor", AvatarCategory.MALE, "male_cyber", 0xFF701A75, 0xFFC026D3),
        AvatarItem("m_5", "Master Chen", AvatarCategory.MALE, "male_master", 0xFF78350F, 0xFFF59E0B),
        // 10 Country & Cultural Style Boys
        AvatarItem("m_ind", "Indian Grandmaster", AvatarCategory.MALE, "boy_india", 0xFFC2410C, 0xFFF59E0B),
        AvatarItem("m_pun", "Punjabi Sardar", AvatarCategory.MALE, "boy_punjab", 0xFF1E3A8A, 0xFFF59E0B),
        AvatarItem("m_pak", "Pakistani Sherwani", AvatarCategory.MALE, "boy_pakistan", 0xFF064E3B, 0xFF10B981),
        AvatarItem("m_usa", "American Champ", AvatarCategory.MALE, "boy_usa", 0xFF1E3A8A, 0xFFEF4444),
        AvatarItem("m_rus", "Russian Master", AvatarCategory.MALE, "boy_russia", 0xFF334155, 0xFF64748B),
        AvatarItem("m_jpn", "Tokyo Prodigy", AvatarCategory.MALE, "boy_japan", 0xFF0F172A, 0xFF06B6D4),
        AvatarItem("m_ara", "Arabian Sheikh", AvatarCategory.MALE, "boy_arabia", 0xFF78350F, 0xFFD97706),
        AvatarItem("m_uk", "British Gentleman", AvatarCategory.MALE, "boy_uk", 0xFF451A03, 0xFFB45309),
        AvatarItem("m_bra", "Brazilian Maestro", AvatarCategory.MALE, "boy_brazil", 0xFF15803D, 0xFFFACC15),
        AvatarItem("m_afr", "African King", AvatarCategory.MALE, "boy_africa", 0xFF92400E, 0xFFEA580C),

        // 3D Cartoon Girls & Queens (Global & Cultural Styles)
        AvatarItem("f_1", "3D Girl Queen", AvatarCategory.FEMALE, "female_queen", 0xFF831843, 0xFFEC4899, drawableResId = com.example.R.drawable.img_avatar_girl),
        AvatarItem("f_2", "Cyber Maya 3D", AvatarCategory.FEMALE, "female_cyber", 0xFF3730A3, 0xFF818CF8),
        AvatarItem("f_3", "Tactician Sophia", AvatarCategory.FEMALE, "female_tactician", 0xFF047857, 0xFF34D399),
        AvatarItem("f_4", "Princess Aria 3D", AvatarCategory.FEMALE, "female_princess", 0xFF9D174D, 0xFFF472B6),
        AvatarItem("f_5", "Gamer Zoe", AvatarCategory.FEMALE, "female_gamer", 0xFF1E1B4B, 0xFF6366F1),
        // 10 Country & Cultural Style Girls
        AvatarItem("f_ind", "Indian Rani", AvatarCategory.FEMALE, "girl_india", 0xFF991B1B, 0xFFF59E0B),
        AvatarItem("f_pun", "Punjabi Kudi", AvatarCategory.FEMALE, "girl_punjab", 0xFF86198F, 0xFFFACC15),
        AvatarItem("f_pak", "Pakistani Begum", AvatarCategory.FEMALE, "girl_pakistan", 0xFF065F46, 0xFF34D399),
        AvatarItem("f_usa", "American Star", AvatarCategory.FEMALE, "girl_usa", 0xFF1D4ED8, 0xFFF43F5E),
        AvatarItem("f_rus", "Russian Tsarina", AvatarCategory.FEMALE, "girl_russia", 0xFF1E293B, 0xFF38BDF8),
        AvatarItem("f_jpn", "Tokyo Cherry", AvatarCategory.FEMALE, "girl_japan", 0xFFBE185D, 0xFFF472B6),
        AvatarItem("f_ara", "Arabian Princess", AvatarCategory.FEMALE, "girl_arabia", 0xFF064E3B, 0xFFF59E0B),
        AvatarItem("f_fra", "Parisian Chic", AvatarCategory.FEMALE, "girl_france", 0xFF111827, 0xFFEF4444),
        AvatarItem("f_kor", "Seoul Idol", AvatarCategory.FEMALE, "girl_korea", 0xFF6B21A8, 0xFFC084FC),
        AvatarItem("f_afr", "African Empress", AvatarCategory.FEMALE, "girl_africa", 0xFF0F766E, 0xFFF59E0B),

        // 3D Stylized Animals
        AvatarItem("a_1", "Royal Lion", AvatarCategory.ANIMAL, "animal_lion", 0xFF92400E, 0xFFFBBF24),
        AvatarItem("a_2", "Clever Fox", AvatarCategory.ANIMAL, "animal_fox", 0xFF9A3412, 0xFFFB923C),
        AvatarItem("a_3", "Fierce Tiger", AvatarCategory.ANIMAL, "animal_tiger", 0xFF7C2D12, 0xFFEA580C),
        AvatarItem("a_4", "Swift Wolf", AvatarCategory.ANIMAL, "animal_wolf", 0xFF374151, 0xFF9CA3AF),
        AvatarItem("a_5", "Zen Panda", AvatarCategory.ANIMAL, "animal_panda", 0xFF111827, 0xFFE5E7EB),
        // 10 Additional Animal Styles
        AvatarItem("a_bengal", "Bengal Tiger", AvatarCategory.ANIMAL, "animal_bengal_tiger", 0xFFC2410C, 0xFFF97316),
        AvatarItem("a_snow_leopard", "Snow Leopard", AvatarCategory.ANIMAL, "animal_snow_leopard", 0xFF334155, 0xFF94A3B8),
        AvatarItem("a_bear", "Siberian Bear", AvatarCategory.ANIMAL, "animal_bear", 0xFF451A03, 0xFF78350F),
        AvatarItem("a_stallion", "Arabian Stallion", AvatarCategory.ANIMAL, "animal_horse", 0xFF0F172A, 0xFF475569),
        AvatarItem("a_cheetah", "Golden Cheetah", AvatarCategory.ANIMAL, "animal_cheetah", 0xFFB45309, 0xFFF59E0B),
        AvatarItem("a_polar", "Polar Bear", AvatarCategory.ANIMAL, "animal_polar_bear", 0xFF1E293B, 0xFFE2E8F0),
        AvatarItem("a_eagle", "Golden Hawk", AvatarCategory.ANIMAL, "animal_hawk", 0xFF78350F, 0xFFD97706),
        AvatarItem("a_kangaroo", "Aussie Joey", AvatarCategory.ANIMAL, "animal_kangaroo", 0xFF9A3412, 0xFFEA580C),
        AvatarItem("a_cobra", "King Cobra", AvatarCategory.ANIMAL, "animal_cobra", 0xFF064E3B, 0xFF10B981),
        AvatarItem("a_dragon", "Mystic Dragon", AvatarCategory.ANIMAL, "animal_dragon", 0xFF047857, 0xFF34D399),

        // 3D Stylized Birds
        AvatarItem("b_1", "Majestic Eagle", AvatarCategory.BIRD, "bird_eagle", 0xFF78350F, 0xFFD97706),
        AvatarItem("b_2", "Royal Falcon", AvatarCategory.BIRD, "bird_falcon", 0xFF1E3A8A, 0xFF60A5FA),
        AvatarItem("b_3", "Wise Owl", AvatarCategory.BIRD, "bird_owl", 0xFF4A044E, 0xFFA21CAF),
        AvatarItem("b_4", "Swift Raven", AvatarCategory.BIRD, "bird_raven", 0xFF0F172A, 0xFF475569),
        AvatarItem("b_5", "Solar Phoenix", AvatarCategory.BIRD, "bird_phoenix", 0xFF991B1B, 0xFFEF4444),

        // 3D Stylized Robots
        AvatarItem("r_1", "Cyber Bot 3000", AvatarCategory.ROBOT, "robot_cyber", 0xFF0E7490, 0xFF22D3EE),
        AvatarItem("r_2", "Mech Knight-01", AvatarCategory.ROBOT, "robot_mech", 0xFF1F2937, 0xFF9CA3AF),
        AvatarItem("r_3", "AI Core Alpha", AvatarCategory.ROBOT, "robot_ai", 0xFF4C1D95, 0xFFA855F7),
        AvatarItem("r_4", "Android Titan", AvatarCategory.ROBOT, "robot_titan", 0xFF064E3B, 0xFF10B981),
        AvatarItem("r_5", "Retro Automaton", AvatarCategory.ROBOT, "robot_retro", 0xFF854D0E, 0xFFFACC15)
    )

    fun getAvatar(id: String): AvatarItem {
        return ALL_AVATARS.find { it.id == id } ?: ALL_AVATARS[0]
    }
}
