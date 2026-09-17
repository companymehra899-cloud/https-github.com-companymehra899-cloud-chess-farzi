package com.example.chess.model

import androidx.compose.ui.graphics.Color

enum class AvatarCategory(val title: String) {
    MALE("Male"),
    FEMALE("Female 3D"),
    ANIMAL("Animals"),
    BIRD("Birds"),
    ROBOT("Robots")
}

data class AvatarItem(
    val id: String,
    val name: String,
    val category: AvatarCategory,
    val iconKey: String, // identifies the vector illustration
    val primaryColor: Long,
    val secondaryColor: Long,
    val imageUrl: String
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
        // Male Avatars
        AvatarItem("m_1", "Grandmaster Leo", AvatarCategory.MALE, "male_king", 0xFF1E3A8A, 0xFF3B82F6, "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&auto=format&fit=crop&q=80"),
        AvatarItem("m_2", "Tactician Arthur", AvatarCategory.MALE, "male_tactician", 0xFF312E81, 0xFF6366F1, "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400&auto=format&fit=crop&q=80"),
        AvatarItem("m_3", "Prodigy Alex", AvatarCategory.MALE, "male_young", 0xFF065F46, 0xFF10B981, "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?w=400&auto=format&fit=crop&q=80"),
        AvatarItem("m_4", "Cyber Victor", AvatarCategory.MALE, "male_cyber", 0xFF701A75, 0xFFC026D3, "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&auto=format&fit=crop&q=80"),
        AvatarItem("m_5", "Master Chen", AvatarCategory.MALE, "male_master", 0xFF78350F, 0xFFF59E0B, "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=400&auto=format&fit=crop&q=80"),

        // Female Cartoon 3D Vector Avatars
        AvatarItem("f_1", "Queen Elena", AvatarCategory.FEMALE, "female_queen", 0xFF831843, 0xFFEC4899, "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=400&auto=format&fit=crop&q=80"),
        AvatarItem("f_2", "Cyber Maya 3D", AvatarCategory.FEMALE, "female_cyber", 0xFF3730A3, 0xFF818CF8, "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=400&auto=format&fit=crop&q=80"),
        AvatarItem("f_3", "Tactician Sophia", AvatarCategory.FEMALE, "female_tactician", 0xFF047857, 0xFF34D399, "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=400&auto=format&fit=crop&q=80"),
        AvatarItem("f_4", "Princess Aria 3D", AvatarCategory.FEMALE, "female_princess", 0xFF9D174D, 0xFFF472B6, "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=400&auto=format&fit=crop&q=80"),
        AvatarItem("f_5", "Gamer Zoe", AvatarCategory.FEMALE, "female_gamer", 0xFF1E1B4B, 0xFF6366F1, "https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?w=400&auto=format&fit=crop&q=80"),

        // Animal Avatars
        AvatarItem("a_1", "Royal Lion", AvatarCategory.ANIMAL, "animal_lion", 0xFF92400E, 0xFFFBBF24, "https://images.unsplash.com/photo-1534188753412-3e26d1d618d6?w=400&auto=format&fit=crop&q=80"),
        AvatarItem("a_2", "Clever Fox", AvatarCategory.ANIMAL, "animal_fox", 0xFF9A3412, 0xFFFB923C, "https://images.unsplash.com/photo-1474511320723-9a56873867b5?w=400&auto=format&fit=crop&q=80"),
        AvatarItem("a_3", "Fierce Tiger", AvatarCategory.ANIMAL, "animal_tiger", 0xFF7C2D12, 0xFFEA580C, "https://images.unsplash.com/photo-1561731216-c3a4d99437d5?w=400&auto=format&fit=crop&q=80"),
        AvatarItem("a_4", "Swift Wolf", AvatarCategory.ANIMAL, "animal_wolf", 0xFF374151, 0xFF9CA3AF, "https://images.unsplash.com/photo-1563460714737-3398ac7f0a5a?w=400&auto=format&fit=crop&q=80"),
        AvatarItem("a_5", "Zen Panda", AvatarCategory.ANIMAL, "animal_panda", 0xFF111827, 0xFFE5E7EB, "https://images.unsplash.com/photo-1564349683136-77e08dba1ef7?w=400&auto=format&fit=crop&q=80"),

        // Birds Avatars
        AvatarItem("b_1", "Majestic Eagle", AvatarCategory.BIRD, "bird_eagle", 0xFF78350F, 0xFFD97706, "https://images.unsplash.com/photo-1616803138400-985a9bc0fbdc?w=400&auto=format&fit=crop&q=80"),
        AvatarItem("b_2", "Royal Falcon", AvatarCategory.BIRD, "bird_falcon", 0xFF1E3A8A, 0xFF60A5FA, "https://images.unsplash.com/photo-1518991855217-863a21381027?w=400&auto=format&fit=crop&q=80"),
        AvatarItem("b_3", "Wise Owl", AvatarCategory.BIRD, "bird_owl", 0xFF4A044E, 0xFFA21CAF, "https://images.unsplash.com/photo-1543599538-a1c8e712a83f?w=400&auto=format&fit=crop&q=80"),
        AvatarItem("b_4", "Swift Raven", AvatarCategory.BIRD, "bird_raven", 0xFF0F172A, 0xFF475569, "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=400&auto=format&fit=crop&q=80"),
        AvatarItem("b_5", "Solar Phoenix", AvatarCategory.BIRD, "bird_phoenix", 0xFF991B1B, 0xFFEF4444, "https://images.unsplash.com/photo-1555685812-4b943f1cb0eb?w=400&auto=format&fit=crop&q=80"),

        // Robot Characters
        AvatarItem("r_1", "Cyber Bot 3000", AvatarCategory.ROBOT, "robot_cyber", 0xFF0E7490, 0xFF22D3EE, "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=400&auto=format&fit=crop&q=80"),
        AvatarItem("r_2", "Mech Knight-01", AvatarCategory.ROBOT, "robot_mech", 0xFF1F2937, 0xFF9CA3AF, "https://images.unsplash.com/photo-1535378917042-10a22c95931a?w=400&auto=format&fit=crop&q=80"),
        AvatarItem("r_3", "AI Core Alpha", AvatarCategory.ROBOT, "robot_ai", 0xFF4C1D95, 0xFFA855F7, "https://images.unsplash.com/photo-1620712943543-bcc4688e7485?w=400&auto=format&fit=crop&q=80"),
        AvatarItem("r_4", "Android Titan", AvatarCategory.ROBOT, "robot_titan", 0xFF064E3B, 0xFF10B981, "https://images.unsplash.com/photo-1614680376593-902f749f7ffc?w=400&auto=format&fit=crop&q=80"),
        AvatarItem("r_5", "Retro Automaton", AvatarCategory.ROBOT, "robot_retro", 0xFF854D0E, 0xFFFACC15, "https://images.unsplash.com/photo-1508739773434-c26b3d09e071?w=400&auto=format&fit=crop&q=80")
    )

    fun getAvatar(id: String): AvatarItem {
        return ALL_AVATARS.find { it.id == id } ?: ALL_AVATARS[0]
    }
}
