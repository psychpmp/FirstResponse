package com.example.android.firstresponse

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import androidx.core.content.ContextCompat

class WhatsWrongActivity : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var optionsSpinner: Spinner
    private lateinit var nextButton: Button
    private lateinit var resultTextView: TextView

    private var currentStage = 0
    private var painLocation = ""
    private var currentQuestions: List<Pair<String, List<String>>> = emptyList()
    private val answersSelected = mutableMapOf<String, String>()

    // Define questions and conditions for each pain location
    private val stages = listOf(
        "When did the symptom start?",
        "Where does the pain or discomfort originate?",
        // Add more stages if needed
    )

    val painLocationQuestions = mapOf(
        "Head" to listOf(
            "Do you have a headache?" to listOf("Yes", "No"),
            "Is the pain localized or diffuse?" to listOf("Localized", "Diffuse"),
            "Do you experience nausea?" to listOf("Yes", "No"),
            "Do you have sensitivity to light?" to listOf("Yes", "No"),
            "Is the headache severe?" to listOf("Yes", "No"),
            "Does the pain get worse with activity?" to listOf("Yes", "No"),
            "Have you had similar headaches before?" to listOf("Yes", "No"),
            "Are you experiencing any vision changes?" to listOf("Yes", "No"),
            "Do you have any other symptoms?" to listOf("Yes", "No"),
            "Is there a family history of headaches?" to listOf("Yes", "No")
        ),
        "Chest" to listOf(
            "Do you have chest pain?" to listOf("Yes", "No"),
            "Is the pain sharp or dull?" to listOf("Sharp", "Dull"),
            "Does the pain radiate to other areas?" to listOf("Yes", "No"),
            "Do you have difficulty breathing?" to listOf("Yes", "No"),
            "Is the pain constant or intermittent?" to listOf("Constant", "Intermittent"),
            "Do you experience nausea?" to listOf("Yes", "No"),
            "Is the pain related to physical activity?" to listOf("Yes", "No"),
            "Do you have a history of any chronic health conditions?" to listOf("Yes", "No"),
            "Are you experiencing sweating/bloating/wheezing/tenderness?" to listOf("Yes", "No"),
            "Do you have any other symptoms?" to listOf("Yes", "No")
        ),
        "Abdomen" to listOf(
            "Do you have abdominal pain?" to listOf("Yes", "No"),
            "Is the pain localized or diffuse?" to listOf("Localized", "Diffuse"),
            "Is the pain cramping or sharp?" to listOf("Cramping", "Sharp"),
            "Do you have nausea or vomiting?" to listOf("Yes", "No"),
            "Do you experience bloating?" to listOf("Yes", "No"),
            "Is there any change in bowel movements?" to listOf("Yes", "No"),
            "Is there blood in the stool?" to listOf("Yes", "No"),
            "Do you have a fever?" to listOf("Yes", "No"),
            "Do you feel better after eating?" to listOf("Yes", "No"),
            "Do you have any other symptoms?" to listOf("Yes", "No")
        ),
        "Back" to listOf(
            "Do you have back pain?" to listOf("Yes", "No"),
            "Is the pain in the upper or lower back?" to listOf("Upper", "Lower"),
            "Is the pain sharp or dull?" to listOf("Sharp", "Dull"),
            "Does the pain radiate to the legs?" to listOf("Yes", "No"),
            "Do you have any weakness in the legs?" to listOf("Yes", "No"),
            "Do you have difficulty walking?" to listOf("Yes", "No"),
            "Is the pain worse when standing or sitting?" to listOf("Standing", "Sitting"),
            "Do you have a history of back problems?" to listOf("Yes", "No"),
            "Do you experience tingling or numbness?" to listOf("Yes", "No"),
            "Do you have any other symptoms?" to listOf("Yes", "No")
        ),
        "Legs" to listOf(
            "Do you have leg pain?" to listOf("Yes", "No"),
            "Is the pain in one or both legs?" to listOf("One", "Both"),
            "Is the pain sharp or dull?" to listOf("Sharp", "Dull"),
            "Do you have swelling in the legs?" to listOf("Yes", "No"),
            "Do you experience cramping?" to listOf("Yes", "No"),
            "Is the pain worse when walking?" to listOf("Yes", "No"),
            "Do you have a history of leg injuries?" to listOf("Yes", "No"),
            "Do you experience tingling or numbness?" to listOf("Yes", "No"),
            "Is there any change in skin color?" to listOf("Yes", "No"),
            "Do you have any other symptoms?" to listOf("Yes", "No")
        ),
        "Arms" to listOf(
            "Do you have arm pain?" to listOf("Yes", "No"),
            "Is the pain in one or both arms?" to listOf("One", "Both"),
            "Is the pain sharp or dull?" to listOf("Sharp", "Dull"),
            "Do you have weakness in the arms?" to listOf("Yes", "No"),
            "Do you experience tingling or numbness?" to listOf("Yes", "No"),
            "Is the pain related to physical activity?" to listOf("Yes", "No"),
            "Do you have swelling in the arms?" to listOf("Yes", "No"),
            "Do you experience muscle cramps?" to listOf("Yes", "No"),
            "Do you have difficulty moving the arms?" to listOf("Yes", "No"),
            "Do you have any other symptoms?" to listOf("Yes", "No")
        ),
        "Joints" to listOf(
            "Do you have joint pain?" to listOf("Yes", "No"),
            "Is the pain in one or multiple joints?" to listOf("One", "Multiple"),
            "Is there any swelling in the joints?" to listOf("Yes", "No"),
            "Do you have stiffness in the joints?" to listOf("Yes", "No"),
            "Is the pain constant or intermittent?" to listOf("Constant", "Intermittent"),
            "Is there redness around the joints?" to listOf("Yes", "No"),
            "Do you have a history of arthritis?" to listOf("Yes", "No"),
            "Does the pain worsen with activity?" to listOf("Yes", "No"),
            "Do you have difficulty moving the joint?" to listOf("Yes", "No"),
            "Do you have any other symptoms?" to listOf("Yes", "No")
        ),
        "Feet" to listOf(
            "Do you have foot pain?" to listOf("Yes", "No"),
            "Is the pain in one or both feet?" to listOf("One", "Both"),
            "Do you have swelling in the feet?" to listOf("Yes", "No"),
            "Do you experience tingling or numbness?" to listOf("Yes", "No"),
            "Is the pain sharp or dull?" to listOf("Sharp", "Dull"),
            "Do you have difficulty walking?" to listOf("Yes", "No"),
            "Do you have a history of foot injuries?" to listOf("Yes", "No"),
            "Is there any redness around the pain area?" to listOf("Yes", "No"),
            "Do you experience burning sensation?" to listOf("Yes", "No"),
            "Do you have any other symptoms?" to listOf("Yes", "No")
        ),
        "Neck" to listOf(
            "Do you have neck pain?" to listOf("Yes", "No"),
            "Is the pain sharp or dull?" to listOf("Sharp", "Dull"),
            "Is the pain worse with movement?" to listOf("Yes", "No"),
            "Do you experience stiffness in the neck?" to listOf("Yes", "No"),
            "Do you have a history of neck injuries?" to listOf("Yes", "No"),
            "Do you experience tingling in the arms?" to listOf("Yes", "No"),
            "Is there any swelling in the neck?" to listOf("Yes", "No"),
            "Is the pain related to posture?" to listOf("Yes", "No"),
            "Do you experience headaches with neck pain?" to listOf("Yes", "No"),
            "Do you have any other symptoms?" to listOf("Yes", "No")
        ),
        "Hands" to listOf(
            "Do you have hand pain?" to listOf("Yes", "No"),
            "Is the pain in one or both hands?" to listOf("One", "Both"),
            "Do you experience tingling or numbness?" to listOf("Yes", "No"),
            "Is there any swelling in the hands?" to listOf("Yes", "No"),
            "Is the pain sharp or dull?" to listOf("Sharp", "Dull"),
            "Do you have difficulty gripping objects?" to listOf("Yes", "No"),
            "Do you experience cramps in the hands?" to listOf("Yes", "No"),
            "Is there any redness around the pain area?" to listOf("Yes", "No"),
            "Do you have a history of hand injuries?" to listOf("Yes", "No"),
            "Do you have any other symptoms?" to listOf("Yes", "No")
        ),
        "Stomach" to listOf(
            "Do you have stomach pain?" to listOf("Yes", "No"),
            "Is the pain sharp or cramping?" to listOf("Sharp", "Cramping"),
            "Do you experience nausea or vomiting?" to listOf("Yes", "No"),
            "Is the pain constant or intermittent?" to listOf("Constant", "Intermittent"),
            "Do you have a fever?" to listOf("Yes", "No"),
            "Do you have any changes in appetite?" to listOf("Yes", "No"),
            "Is there bloating?" to listOf("Yes", "No"),
            "Do you experience diarrhea or constipation?" to listOf("Yes", "No"),
            "Is there any tenderness to touch?" to listOf("Yes", "No"),
            "Do you have any other symptoms?" to listOf("Yes", "No")
        )
    )


    private val diagnosis = mapOf(
        "Head" to listOf(
            listOf(
                "Do you have a headache?" to "Yes",
                "Is the pain localized or diffuse?" to "Localized",
                "Do you experience nausea?" to "Yes",
                "Do you have sensitivity to light?" to "Yes",
                "Is the headache severe?" to "Yes",
                "Does the pain get worse with activity?" to "Yes",
                "Have you had similar headaches before?" to "Yes",
                "Are you experiencing any vision changes?" to "No",
                "Do you have any other symptoms?" to "No",
                "Is there a family history of headaches?" to "Yes"
            ) to "Migraine",
            listOf(
                "Do you have a headache?" to "Yes",
                "Is the pain localized or diffuse?" to "Diffuse",
                "Do you experience nausea?" to "No",
                "Do you have sensitivity to light?" to "No",
                "Is the headache severe?" to "No",
                "Does the pain get worse with stress?" to "Yes",
                "Have you had similar headaches before?" to "Yes",
                "Are you experiencing any vision changes?" to "No",
                "Do you have any other symptoms?" to "No",
                "Is there a family history of headaches?" to "No"
            ) to "Tension Headache",
            listOf(
                "Do you have a headache?" to "Yes",
                "Is the pain localized or diffuse?" to "Localized",
                "Do you experience nausea?" to "No",
                "Do you have sensitivity to light?" to "No",
                "Is the headache severe?" to "Yes",
                "Does the pain get worse with lying down?" to "Yes",
                "Have you had similar headaches before?" to "No",
                "Are you experiencing any vision changes?" to "Yes",
                "Do you have any other symptoms?" to "Yes",
                "Is there a family history of headaches?" to "Yes"
            ) to "Concussion",
            listOf(
                "Do you have a headache?" to "Yes",
                "Is the pain localized or diffuse?" to "Localized",
                "Do you experience nausea?" to "No",
                "Do you have sensitivity to light?" to "Yes",
                "Is the headache severe?" to "Yes",
                "Does the pain get worse with movement?" to "Yes",
                "Have you had similar headaches before?" to "No",
                "Are you experiencing any vision changes?" to "Yes",
                "Do you have any other symptoms?" to "Yes",
                "Is there a family history of headaches?" to "Yes"
            ) to "Meningitis",
            listOf(
                "Do you have a headache?" to "Yes",
                "Is the pain localized or diffuse?" to "Localized",
                "Do you experience nausea?" to "Yes",
                "Do you have sensitivity to light?" to "No",
                "Is the headache severe?" to "Yes",
                "Does the pain get worse with physical activity?" to "No",
                "Have you had similar headaches before?" to "No",
                "Are you experiencing any vision changes?" to "Yes",
                "Do you have any other symptoms?" to "Yes",
                "Is there a family history of headaches?" to "Yes"
            ) to "Stroke"
        ),
        "Chest" to listOf(
            listOf(
                "Do you have chest pain?" to "Yes",
                "Is the pain sharp or dull?" to "Sharp",
                "Does the pain radiate to other areas?" to "Yes",
                "Do you have difficulty breathing?" to "No",
                "Is the pain constant or intermittent?" to "Constant",
                "Do you experience nausea?" to "No",
                "Is the pain related to physical activity?" to "Yes",
                "Do you have a history of any chronic health conditions?" to "No",
                "Are you experiencing sweating/bloating/wheezing/tenderness?" to "No",
                "Do you have any other symptoms?" to "No"
            ) to "Angina",
            listOf(
                "Do you have chest pain?" to "Yes",
                "Is the pain sharp or dull?" to "Sharp",
                "Does the pain radiate to other areas?" to "Yes",
                "Do you have difficulty breathing?" to "Yes",
                "Is the pain constant or intermittent?" to "Constant",
                "Do you experience nausea?" to "Yes",
                "Is the pain related to physical activity?" to "No",
                "Do you have a history of any chronic health conditions?" to "Yes",
                "Are you experiencing sweating/bloating/wheezing/tenderness?" to "Yes",
                "Do you have any other symptoms?" to "Yes"
            ) to "Heart Attack",
            listOf(
                "Do you have chest pain?" to "Yes",
                "Is the pain sharp or dull?" to "Dull",
                "Does the pain radiate to other areas?" to "No",
                "Do you have difficulty breathing?" to "Yes",
                "Is the pain constant or intermittent?" to "Intermittent",
                "Do you experience nausea?" to "No",
                "Is the pain related to physical activity?" to "No",
                "Do you have a history of any chronic health conditions?" to "No",
                "Are you experiencing sweating/bloating/wheezing/tenderness?" to "No",
                "Do you have any other symptoms?" to "No"
            ) to "Gastroesophageal Reflux Disease (GERD)",
            listOf(
                "Do you have chest pain?" to "Yes",
                "Is the pain sharp or dull?" to "Sharp",
                "Does the pain radiate to other areas?" to "No",
                "Do you have difficulty breathing?" to "No",
                "Is the pain constant or intermittent?" to "Constant",
                "Do you experience nausea?" to "Yes",
                "Is the pain related to physical activity?" to "No",
                "Do you have a history of any chronic health conditions?" to "No",
                "Are you experiencing sweating/bloating/wheezing/tenderness?" to "Yes",
                "Do you have any other symptoms?" to "Yes"
            ) to "Panic Attack",
            listOf(
                "Do you have chest pain?" to "Yes",
                "Is the pain sharp or dull?" to "Dull",
                "Does the pain radiate to other areas?" to "Yes",
                "Do you have difficulty breathing?" to "Yes",
                "Is the pain constant or intermittent?" to "Intermittent",
                "Do you experience nausea?" to "No",
                "Is the pain related to physical activity?" to "No",
                "Do you have a history of any chronic health conditions?" to "No",
                "Are you experiencing sweating/bloating/wheezing/tenderness?" to "Yes",
                "Do you have any other symptoms?" to "Yes"
            ) to "Pulmonary Embolism",
            listOf(
                "Do you have chest pain?" to "Yes",
                "Is the pain sharp or dull?" to "Dull",
                "Does the pain radiate to other areas?" to "No",
                "Do you have difficulty breathing?" to "No",
                "Is the pain constant or intermittent?" to "Constant",
                "Do you experience nausea?" to "No",
                "Is the pain related to eating?" to "Yes",
                "Do you have a history of any chronic health conditions?" to "Yes",
                "Are you experiencing sweating/bloating/wheezing/tenderness?" to "Yes",
                "Do you have any other symptoms?" to "No"
            ) to "Peptic Ulcer",
            listOf(
                "Do you have chest pain?" to "Yes",
                "Is the pain sharp or dull?" to "Sharp",
                "Does the pain radiate to other areas?" to "No",
                "Do you have difficulty breathing?" to "Yes",
                "Is the pain constant or intermittent?" to "Intermittent",
                "Do you experience shortness of breath?" to "Yes",
                "Is the pain related to cold weather?" to "Yes",
                "Do you have a history of any chronic health conditions?" to "Yes",
                "Are you experiencing sweating/bloating/wheezing/tenderness?" to "Yes",
                "Do you have any other symptoms?" to "No"
            ) to "Asthma",
            listOf(
                "Do you have chest pain?" to "Yes",
                "Is the pain sharp or dull?" to "Sharp",
                "Does the pain radiate to other areas?" to "Yes",
                "Do you have difficulty breathing?" to "No",
                "Is the pain constant or intermittent?" to "Intermittent",
                "Do you experience nausea?" to "No",
                "Is the pain related to physical activity?" to "Yes",
                "Do you have a history of any chronic health conditions?" to "Yes",
                "Are you experiencing sweating/bloating/wheezing/tenderness?" to "No",
                "Do you have any other symptoms?" to "No"
            ) to "Muscle Strain"
        ),
        "Abdomen" to listOf(
            listOf(
                "Do you have abdominal pain?" to "Yes",
                "Is the pain localized or diffuse?" to "Localized",
                "Is the pain cramping or sharp?" to "Cramping",
                "Do you have nausea or vomiting?" to "Yes",
                "Do you experience bloating?" to "Yes",
                "Is there any change in bowel movements?" to "Yes",
                "Is there blood in the stool?" to "No",
                "Do you have a fever?" to "No",
                "Do you feel better after eating?" to "No",
                "Do you have any other symptoms?" to "No"
            ) to "Gastritis",
            listOf(
                "Do you have abdominal pain?" to "Yes",
                "Is the pain localized or diffuse?" to "Diffuse",
                "Is the pain cramping or sharp?" to "Sharp",
                "Do you have nausea or vomiting?" to "Yes",
                "Do you experience bloating?" to "Yes",
                "Is there any change in bowel movements?" to "No",
                "Is there blood in the stool?" to "Yes",
                "Do you have a fever?" to "Yes",
                "Do you feel better after eating?" to "No",
                "Do you have any other symptoms?" to "Yes"
            ) to "Appendicitis",
            listOf(
                "Do you have abdominal pain?" to "Yes",
                "Is the pain localized or diffuse?" to "Localized",
                "Is the pain cramping or sharp?" to "Cramping",
                "Do you have nausea or vomiting?" to "Yes",
                "Do you experience bloating?" to "Yes",
                "Is there any change in bowel movements?" to "Yes",
                "Is there blood in the stool?" to "No",
                "Do you have a fever?" to "No",
                "Do you feel better after eating?" to "Yes",
                "Do you have any other symptoms?" to "No"
            ) to "Irritable Bowel Syndrome (IBS)",
            listOf(
                "Do you have abdominal pain?" to "Yes",
                "Is the pain localized or diffuse?" to "Diffuse",
                "Is the pain cramping or sharp?" to "Sharp",
                "Do you have nausea or vomiting?" to "No",
                "Do you experience bloating?" to "Yes",
                "Is there any change in bowel movements?" to "No",
                "Is there blood in the stool?" to "No",
                "Do you have a fever?" to "No",
                "Do you feel better after eating?" to "No",
                "Do you have any other symptoms?" to "Yes"
            ) to "Peptic Ulcer Disease",
            listOf(
                "Do you have abdominal pain?" to "Yes",
                "Is the pain localized or diffuse?" to "Localized",
                "Is the pain cramping or sharp?" to "Sharp",
                "Do you have nausea or vomiting?" to "No",
                "Do you experience bloating?" to "No",
                "Is there any change in bowel movements?" to "Yes",
                "Is there blood in the stool?" to "No",
                "Do you have a fever?" to "Yes",
                "Do you feel better after eating?" to "No",
                "Do you have any other symptoms?" to "Yes"
            ) to "Diverticulitis",
            listOf(
                "Do you have abdominal pain?" to "Yes",
                "Is the pain localized or diffuse?" to "Diffuse",
                "Is the pain cramping or sharp?" to "Cramping",
                "Do you have nausea or vomiting?" to "Yes",
                "Do you experience bloating?" to "Yes",
                "Is there any change in bowel movements?" to "No",
                "Is there blood in the stool?" to "No",
                "Do you have a fever?" to "No",
                "Do you feel better after eating?" to "Yes",
                "Do you have any other symptoms?" to "No"
            ) to "Constipation",
            listOf(
                "Do you have abdominal pain?" to "Yes",
                "Is the pain localized or diffuse?" to "Localized",
                "Is the pain cramping or sharp?" to "Cramping",
                "Do you have nausea or vomiting?" to "Yes",
                "Do you experience bloating?" to "Yes",
                "Is there any change in bowel movements?" to "Yes",
                "Is there blood in the stool?" to "No",
                "Do you have a fever?" to "Yes",
                "Do you feel better after eating?" to "No",
                "Do you have any other symptoms?" to "Yes"
            ) to "Food Poisoning",
            listOf(
                "Do you have abdominal pain?" to "Yes",
                "Is the pain localized or diffuse?" to "Diffuse",
                "Is the pain cramping or sharp?" to "Sharp",
                "Do you have nausea or vomiting?" to "Yes",
                "Do you experience bloating?" to "No",
                "Is there any change in bowel movements?" to "Yes",
                "Is there blood in the stool?" to "Yes",
                "Do you have a fever?" to "Yes",
                "Do you feel better after eating?" to "No",
                "Do you have any other symptoms?" to "No"
            ) to "Inflammatory Bowel Disease (IBD)",
            listOf(
                "Do you have abdominal pain?" to "Yes",
                "Is the pain localized or diffuse?" to "Localized",
                "Is the pain cramping or sharp?" to "Cramping",
                "Do you have nausea or vomiting?" to "No",
                "Do you experience bloating?" to "No",
                "Is there any change in bowel movements?" to "Yes",
                "Is there blood in the stool?" to "No",
                "Do you have a fever?" to "No",
                "Do you feel better after eating?" to "Yes",
                "Do you have any other symptoms?" to "No"
            ) to "Functional Dyspepsia",
            listOf(
                "Do you have abdominal pain?" to "Yes",
                "Is the pain localized or diffuse?" to "Diffuse",
                "Is the pain cramping or sharp?" to "Sharp",
                "Do you have nausea or vomiting?" to "Yes",
                "Do you experience bloating?" to "Yes",
                "Is there any change in bowel movements?" to "No",
                "Is there blood in the stool?" to "No",
                "Do you have a fever?" to "No",
                "Do you feel better after eating?" to "No",
                "Do you have any other symptoms?" to "Yes"
            ) to "Celiac Disease",
            listOf(
                "Do you have abdominal pain?" to "Yes",
                "Is the pain localized or diffuse?" to "Localized",
                "Is the pain cramping or sharp?" to "Cramping",
                "Do you have nausea or vomiting?" to "Yes",
                "Do you experience bloating?" to "No",
                "Is there any change in bowel movements?" to "Yes",
                "Is there blood in the stool?" to "No",
                "Do you have a fever?" to "Yes",
                "Do you feel better after eating?" to "No",
                "Do you have any other symptoms?" to "No"
            ) to "Lactose Intolerance"
        ),
        "Back" to listOf(
            listOf(
                "Do you have back pain?" to "Yes",
                "Is the pain in the upper or lower back?" to "Lower",
                "Is the pain sharp or dull?" to "Sharp",
                "Does the pain radiate to the legs?" to "Yes",
                "Do you have any weakness in the legs?" to "No",
                "Do you have difficulty walking?" to "Yes",
                "Is the pain worse when standing or sitting?" to "Standing",
                "Do you have a history of back problems?" to "Yes",
                "Do you experience tingling or numbness?" to "Yes",
                "Do you have any other symptoms?" to "No"
            ) to "Herniated Disc",
            listOf(
                "Do you have back pain?" to "Yes",
                "Is the pain in the upper or lower back?" to "Upper",
                "Is the pain sharp or dull?" to "Dull",
                "Does the pain radiate to the legs?" to "No",
                "Do you have any weakness in the legs?" to "No",
                "Do you have difficulty walking?" to "No",
                "Is the pain worse when standing or sitting?" to "Sitting",
                "Do you have a history of back problems?" to "Yes",
                "Do you experience tingling or numbness?" to "No",
                "Do you have any other symptoms?" to "No"
            ) to "Muscle Strain",
            listOf(
                "Do you have back pain?" to "Yes",
                "Is the pain in the upper or lower back?" to "Lower",
                "Is the pain sharp or dull?" to "Dull",
                "Does the pain radiate to the legs?" to "Yes",
                "Do you have any weakness in the legs?" to "Yes",
                "Do you have difficulty walking?" to "Yes",
                "Is the pain worse when standing or sitting?" to "Standing",
                "Do you have a history of back problems?" to "No",
                "Do you experience tingling or numbness?" to "Yes",
                "Do you have any other symptoms?" to "Yes"
            ) to "Spinal Stenosis",
            listOf(
                "Do you have back pain?" to "Yes",
                "Is the pain in the upper or lower back?" to "Upper",
                "Is the pain sharp or dull?" to "Sharp",
                "Does the pain radiate to the legs?" to "No",
                "Do you have any weakness in the legs?" to "No",
                "Do you have difficulty walking?" to "No",
                "Is the pain worse when standing or sitting?" to "Sitting",
                "Do you have a history of back problems?" to "No",
                "Do you experience tingling or numbness?" to "No",
                "Do you have any other symptoms?" to "Yes"
            ) to "Postural Issues",
            listOf(
                "Do you have back pain?" to "Yes",
                "Is the pain in the upper or lower back?" to "Lower",
                "Is the pain sharp or dull?" to "Sharp",
                "Does the pain radiate to the legs?" to "Yes",
                "Do you have any weakness in the legs?" to "Yes",
                "Do you have difficulty walking?" to "No",
                "Is the pain worse when standing or sitting?" to "Standing",
                "Do you have a history of back problems?" to "Yes",
                "Do you experience tingling or numbness?" to "No",
                "Do you have any other symptoms?" to "No"
            ) to "Sciatica",
            listOf(
                "Do you have back pain?" to "Yes",
                "Is the pain in the upper or lower back?" to "Lower",
                "Is the pain sharp or dull?" to "Dull",
                "Does the pain radiate to the legs?" to "No",
                "Do you have any weakness in the legs?" to "No",
                "Do you have difficulty walking?" to "No",
                "Is the pain worse when standing or sitting?" to "Standing",
                "Do you have a history of back problems?" to "No",
                "Do you experience tingling or numbness?" to "No",
                "Do you have any other symptoms?" to "Yes"
            ) to "Kidney Stones",
            listOf(
                "Do you have back pain?" to "Yes",
                "Is the pain in the upper or lower back?" to "Upper",
                "Is the pain sharp or dull?" to "Sharp",
                "Does the pain radiate to the legs?" to "No",
                "Do you have any weakness in the legs?" to "No",
                "Do you have difficulty walking?" to "No",
                "Is the pain worse when standing or sitting?" to "Sitting",
                "Do you have a history of back problems?" to "Yes",
                "Do you experience tingling or numbness?" to "No",
                "Do you have any other symptoms?" to "No"
            ) to "Thoracic Outlet Syndrome",
            listOf(
                "Do you have back pain?" to "Yes",
                "Is the pain in the upper or lower back?" to "Lower",
                "Is the pain sharp or dull?" to "Dull",
                "Does the pain radiate to the legs?" to "Yes",
                "Do you have any weakness in the legs?" to "Yes",
                "Do you have difficulty walking?" to "Yes",
                "Is the pain worse when standing or sitting?" to "Standing",
                "Do you have a history of back problems?" to "Yes",
                "Do you experience tingling or numbness?" to "Yes",
                "Do you have any other symptoms?" to "Yes"
            ) to "Degenerative Disc Disease",
            listOf(
                "Do you have back pain?" to "Yes",
                "Is the pain in the upper or lower back?" to "Upper",
                "Is the pain sharp or dull?" to "Sharp",
                "Does the pain radiate to the legs?" to "No",
                "Do you have any weakness in the legs?" to "No",
                "Do you have difficulty walking?" to "No",
                "Is the pain worse when standing or sitting?" to "Sitting",
                "Do you have a history of back problems?" to "No",
                "Do you experience tingling or numbness?" to "No",
                "Do you have any other symptoms?" to "No"
            ) to "Upper Back Pain",
            listOf(
                "Do you have back pain?" to "Yes",
                "Is the pain in the upper or lower back?" to "Lower",
                "Is the pain sharp or dull?" to "Dull",
                "Does the pain radiate to the legs?" to "No",
                "Do you have any weakness in the legs?" to "No",
                "Do you have difficulty walking?" to "No",
                "Is the pain worse when standing or sitting?" to "Standing",
                "Do you have a history of back problems?" to "No",
                "Do you experience tingling or numbness?" to "Yes",
                "Do you have any other symptoms?" to "No"
            ) to "Muscle Spasm"
        ),
        "Legs" to listOf(
            listOf(
                "Do you have leg pain?" to "Yes",
                "Is the pain in one or both legs?" to "One",
                "Is the pain sharp or dull?" to "Sharp",
                "Do you have swelling in the legs?" to "Yes",
                "Do you experience cramping?" to "No",
                "Is the pain worse when walking?" to "Yes",
                "Do you have a history of leg injuries?" to "No",
                "Do you experience tingling or numbness?" to "Yes",
                "Is there any change in skin color?" to "No",
                "Do you have any other symptoms?" to "No"
            ) to "Deep Vein Thrombosis (DVT)",
            listOf(
                "Do you have leg pain?" to "Yes",
                "Is the pain in one or both legs?" to "Both",
                "Is the pain sharp or dull?" to "Dull",
                "Do you have swelling in the legs?" to "Yes",
                "Do you experience cramping?" to "Yes",
                "Is the pain worse when walking?" to "No",
                "Do you have a history of leg injuries?" to "No",
                "Do you experience tingling or numbness?" to "No",
                "Is there any change in skin color?" to "Yes",
                "Do you have any other symptoms?" to "Yes"
            ) to "Peripheral Artery Disease (PAD)",
            listOf(
                "Do you have leg pain?" to "Yes",
                "Is the pain in one or both legs?" to "One",
                "Is the pain sharp or dull?" to "Sharp",
                "Do you have swelling in the legs?" to "No",
                "Do you experience cramping?" to "No",
                "Is the pain worse when walking?" to "Yes",
                "Do you have a history of leg injuries?" to "Yes",
                "Do you experience tingling or numbness?" to "Yes",
                "Is there any change in skin color?" to "No",
                "Do you have any other symptoms?" to "No"
            ) to "Muscle Strain",
            listOf(
                "Do you have leg pain?" to "Yes",
                "Is the pain in one or both legs?" to "Both",
                "Is the pain sharp or dull?" to "Dull",
                "Do you have swelling in the legs?" to "Yes",
                "Do you experience cramping?" to "Yes",
                "Is the pain worse when walking?" to "Yes",
                "Do you have a history of leg injuries?" to "No",
                "Do you experience tingling or numbness?" to "No",
                "Is there any change in skin color?" to "No",
                "Do you have any other symptoms?" to "No"
            ) to "Varicose Veins",
            listOf(
                "Do you have leg pain?" to "Yes",
                "Is the pain in one or both legs?" to "One",
                "Is the pain sharp or dull?" to "Dull",
                "Do you have swelling in the legs?" to "No",
                "Do you experience cramping?" to "Yes",
                "Is the pain worse when walking?" to "No",
                "Do you have a history of leg injuries?" to "Yes",
                "Do you experience tingling or numbness?" to "Yes",
                "Is there any change in skin color?" to "No",
                "Do you have any other symptoms?" to "Yes"
            ) to "Sciatica",
            listOf(
                "Do you have leg pain?" to "Yes",
                "Is the pain in one or both legs?" to "Both",
                "Is the pain sharp or dull?" to "Sharp",
                "Do you have swelling in the legs?" to "No",
                "Do you experience cramping?" to "No",
                "Is the pain worse when walking?" to "Yes",
                "Do you have a history of leg injuries?" to "No",
                "Do you experience tingling or numbness?" to "No",
                "Is there any change in skin color?" to "Yes",
                "Do you have any other symptoms?" to "No"
            ) to "Cellulitis",
            listOf(
                "Do you have leg pain?" to "Yes",
                "Is the pain in one or both legs?" to "One",
                "Is the pain sharp or dull?" to "Dull",
                "Do you have swelling in the legs?" to "Yes",
                "Do you experience cramping?" to "No",
                "Is the pain worse when walking?" to "No",
                "Do you have a history of leg injuries?" to "Yes",
                "Do you experience tingling or numbness?" to "No",
                "Is there any change in skin color?" to "Yes",
                "Do you have any other symptoms?" to "Yes"
            ) to "Chronic Venous Insufficiency",
            listOf(
                "Do you have leg pain?" to "Yes",
                "Is the pain in one or both legs?" to "Both",
                "Is the pain sharp or dull?" to "Sharp",
                "Do you have swelling in the legs?" to "Yes",
                "Do you experience cramping?" to "Yes",
                "Is the pain worse when walking?" to "Yes",
                "Do you have a history of leg injuries?" to "No",
                "Do you experience tingling or numbness?" to "Yes",
                "Is there any change in skin color?" to "No",
                "Do you have any other symptoms?" to "No"
            ) to "Deep Vein Thrombosis (DVT)",
            listOf(
                "Do you have leg pain?" to "Yes",
                "Is the pain in one or both legs?" to "One",
                "Is the pain sharp or dull?" to "Dull",
                "Do you have swelling in the legs?" to "No",
                "Do you experience cramping?" to "No",
                "Is the pain worse when walking?" to "No",
                "Do you have a history of leg injuries?" to "Yes",
                "Do you experience tingling or numbness?" to "Yes",
                "Is there any change in skin color?" to "No",
                "Do you have any other symptoms?" to "No"
            ) to "Muscle Strain",
            listOf(
                "Do you have leg pain?" to "Yes",
                "Is the pain in one or both legs?" to "Both",
                "Is the pain sharp or dull?" to "Sharp",
                "Do you have swelling in the legs?" to "Yes",
                "Do you experience cramping?" to "Yes",
                "Is the pain worse when walking?" to "Yes",
                "Do you have a history of leg injuries?" to "No",
                "Do you experience tingling or numbness?" to "No",
                "Is there any change in skin color?" to "No",
                "Do you have any other symptoms?" to "Yes"
            ) to "Peripheral Artery Disease (PAD)",
            listOf(
                "Do you have leg pain?" to "Yes",
                "Is the pain in one or both legs?" to "Both",
                "Is the pain sharp or dull?" to "Dull",
                "Do you have swelling in the legs?" to "No",
                "Do you experience cramping?" to "Yes",
                "Is the pain worse when walking?" to "No",
                "Do you have a history of leg injuries?" to "No",
                "Do you experience tingling or numbness?" to "No",
                "Is there any change in skin color?" to "Yes",
                "Do you have any other symptoms?" to "No"
            ) to "Varicose Veins"
        ),
        "Arms" to listOf(
            listOf(
                "Do you have arm pain?" to "Yes",
                "Is the pain in one or both arms?" to "One",
                "Is the pain sharp or dull?" to "Sharp",
                "Do you have weakness in the arms?" to "Yes",
                "Do you experience tingling or numbness?" to "Yes",
                "Is the pain related to physical activity?" to "No",
                "Do you have swelling in the arms?" to "No",
                "Do you experience muscle cramps?" to "No",
                "Do you have difficulty moving the arms?" to "No",
                "Do you have any other symptoms?" to "No"
            ) to "Muscle Strain",
            listOf(
                "Do you have arm pain?" to "Yes",
                "Is the pain in one or both arms?" to "Both",
                "Is the pain sharp or dull?" to "Dull",
                "Do you have weakness in the arms?" to "Yes",
                "Do you experience tingling or numbness?" to "Yes",
                "Is the pain related to physical activity?" to "Yes",
                "Do you have swelling in the arms?" to "Yes",
                "Do you experience muscle cramps?" to "No",
                "Do you have difficulty moving the arms?" to "Yes",
                "Do you have any other symptoms?" to "No"
            ) to "Tendinitis",
            listOf(
                "Do you have arm pain?" to "Yes",
                "Is the pain in one or both arms?" to "One",
                "Is the pain sharp or dull?" to "Sharp",
                "Do you have weakness in the arms?" to "No",
                "Do you experience tingling or numbness?" to "No",
                "Is the pain related to physical activity?" to "No",
                "Do you have swelling in the arms?" to "No",
                "Do you experience muscle cramps?" to "Yes",
                "Do you have difficulty moving the arms?" to "No",
                "Do you have any other symptoms?" to "No"
            ) to "Overuse Syndrome",
            listOf(
                "Do you have arm pain?" to "Yes",
                "Is the pain in one or both arms?" to "Both",
                "Is the pain sharp or dull?" to "Dull",
                "Do you have weakness in the arms?" to "Yes",
                "Do you experience tingling or numbness?" to "Yes",
                "Is the pain related to physical activity?" to "Yes",
                "Do you have swelling in the arms?" to "Yes",
                "Do you experience muscle cramps?" to "No",
                "Do you have difficulty moving the arms?" to "Yes",
                "Do you have any other symptoms?" to "Yes"
            ) to "Carpal Tunnel Syndrome",
            listOf(
                "Do you have arm pain?" to "Yes",
                "Is the pain in one or both arms?" to "One",
                "Is the pain sharp or dull?" to "Sharp",
                "Do you have weakness in the arms?" to "Yes",
                "Do you experience tingling or numbness?" to "Yes",
                "Is the pain related to physical activity?" to "No",
                "Do you have swelling in the arms?" to "Yes",
                "Do you experience muscle cramps?" to "No",
                "Do you have difficulty moving the arms?" to "No",
                "Do you have any other symptoms?" to "No"
            ) to "Bursitis",
            listOf(
                "Do you have arm pain?" to "Yes",
                "Is the pain in one or both arms?" to "Both",
                "Is the pain sharp or dull?" to "Dull",
                "Do you have weakness in the arms?" to "No",
                "Do you experience tingling or numbness?" to "No",
                "Is the pain related to physical activity?" to "No",
                "Do you have swelling in the arms?" to "Yes",
                "Do you experience muscle cramps?" to "Yes",
                "Do you have difficulty moving the arms?" to "No",
                "Do you have any other symptoms?" to "No"
            ) to "Swelling",
            listOf(
                "Do you have arm pain?" to "Yes",
                "Is the pain in one or both arms?" to "One",
                "Is the pain sharp or dull?" to "Dull",
                "Do you have weakness in the arms?" to "Yes",
                "Do you experience tingling or numbness?" to "No",
                "Is the pain related to physical activity?" to "Yes",
                "Do you have swelling in the arms?" to "No",
                "Do you experience muscle cramps?" to "No",
                "Do you have difficulty moving the arms?" to "Yes",
                "Do you have any other symptoms?" to "Yes"
            ) to "Nerve Compression",
            listOf(
                "Do you have arm pain?" to "Yes",
                "Is the pain in one or both arms?" to "Both",
                "Is the pain sharp or dull?" to "Sharp",
                "Do you have weakness in the arms?" to "Yes",
                "Do you experience tingling or numbness?" to "Yes",
                "Is the pain related to physical activity?" to "No",
                "Do you have swelling in the arms?" to "Yes",
                "Do you experience muscle cramps?" to "No",
                "Do you have difficulty moving the arms?" to "Yes",
                "Do you have any other symptoms?" to "Yes"
            ) to "Frozen Shoulder",
            listOf(
                "Do you have arm pain?" to "Yes",
                "Is the pain in one or both arms?" to "One",
                "Is the pain sharp or dull?" to "Dull",
                "Do you have weakness in the arms?" to "Yes",
                "Do you experience tingling or numbness?" to "Yes",
                "Is the pain related to physical activity?" to "Yes",
                "Do you have swelling in the arms?" to "No",
                "Do you experience muscle cramps?" to "No",
                "Do you have difficulty moving the arms?" to "Yes",
                "Do you have any other symptoms?" to "No"
            ) to "Tendinitis",
            listOf(
                "Do you have arm pain?" to "Yes",
                "Is the pain in one or both arms?" to "Both",
                "Is the pain sharp or dull?" to "Sharp",
                "Do you have weakness in the arms?" to "No",
                "Do you experience tingling or numbness?" to "No",
                "Is the pain related to physical activity?" to "No",
                "Do you have swelling in the arms?" to "Yes",
                "Do you experience muscle cramps?" to "Yes",
                "Do you have difficulty moving the arms?" to "No",
                "Do you have any other symptoms?" to "No"
            ) to "Myopathy"
        ),
        "Joints" to listOf(
            listOf(
                "Do you have joint pain?" to "Yes",
                "Is the pain in one or multiple joints?" to "One",
                "Is there any swelling in the joints?" to "Yes",
                "Do you have stiffness in the joints?" to "Yes",
                "Is the pain constant or intermittent?" to "Constant",
                "Is there redness around the joints?" to "No",
                "Do you have a history of arthritis?" to "Yes",
                "Does the pain worsen with activity?" to "Yes",
                "Do you have difficulty moving the joint?" to "Yes",
                "Do you have any other symptoms?" to "No"
            ) to "Osteoarthritis",
            listOf(
                "Do you have joint pain?" to "Yes",
                "Is the pain in one or multiple joints?" to "Multiple",
                "Is there any swelling in the joints?" to "Yes",
                "Do you have stiffness in the joints?" to "Yes",
                "Is the pain constant or intermittent?" to "Intermittent",
                "Is there redness around the joints?" to "Yes",
                "Do you have a history of arthritis?" to "Yes",
                "Does the pain worsen with activity?" to "Yes",
                "Do you have difficulty moving the joint?" to "Yes",
                "Do you have any other symptoms?" to "No"
            ) to "Rheumatoid Arthritis",
            listOf(
                "Do you have joint pain?" to "Yes",
                "Is the pain in one or multiple joints?" to "One",
                "Is there any swelling in the joints?" to "No",
                "Do you have stiffness in the joints?" to "No",
                "Is the pain constant or intermittent?" to "Intermittent",
                "Is there redness around the joints?" to "No",
                "Do you have a history of arthritis?" to "No",
                "Does the pain worsen with activity?" to "Yes",
                "Do you have difficulty moving the joint?" to "No",
                "Do you have any other symptoms?" to "No"
            ) to "Tendinitis",
            listOf(
                "Do you have joint pain?" to "Yes",
                "Is the pain in one or multiple joints?" to "Multiple",
                "Is there any swelling in the joints?" to "Yes",
                "Do you have stiffness in the joints?" to "Yes",
                "Is the pain constant or intermittent?" to "Constant",
                "Is there redness around the joints?" to "Yes",
                "Do you have a history of arthritis?" to "No",
                "Does the pain worsen with activity?" to "Yes",
                "Do you have difficulty moving the joint?" to "Yes",
                "Do you have any other symptoms?" to "Yes"
            ) to "Gout",
            listOf(
                "Do you have joint pain?" to "Yes",
                "Is the pain in one or multiple joints?" to "One",
                "Is there any swelling in the joints?" to "Yes",
                "Do you have stiffness in the joints?" to "Yes",
                "Is the pain constant or intermittent?" to "Intermittent",
                "Is there redness around the joints?" to "No",
                "Do you have a history of arthritis?" to "Yes",
                "Does the pain worsen with activity?" to "No",
                "Do you have difficulty moving the joint?" to "Yes",
                "Do you have any other symptoms?" to "No"
            ) to "Bursitis",
            listOf(
                "Do you have joint pain?" to "Yes",
                "Is the pain in one or multiple joints?" to "Multiple",
                "Is there any swelling in the joints?" to "No",
                "Do you have stiffness in the joints?" to "No",
                "Is the pain constant or intermittent?" to "Constant",
                "Is there redness around the joints?" to "Yes",
                "Do you have a history of arthritis?" to "Yes",
                "Does the pain worsen with activity?" to "Yes",
                "Do you have difficulty moving the joint?" to "No",
                "Do you have any other symptoms?" to "No"
            ) to "Psoriatic Arthritis",
            listOf(
                "Do you have joint pain?" to "Yes",
                "Is the pain in one or multiple joints?" to "One",
                "Is there any swelling in the joints?" to "No",
                "Do you have stiffness in the joints?" to "Yes",
                "Is the pain constant or intermittent?" to "Intermittent",
                "Is there redness around the joints?" to "No",
                "Do you have a history of arthritis?" to "No",
                "Does the pain worsen with activity?" to "Yes",
                "Do you have difficulty moving the joint?" to "No",
                "Do you have any other symptoms?" to "No"
            ) to "Ligament Sprain",
            listOf(
                "Do you have joint pain?" to "Yes",
                "Is the pain in one or multiple joints?" to "Multiple",
                "Is there any swelling in the joints?" to "Yes",
                "Do you have stiffness in the joints?" to "Yes",
                "Is the pain constant or intermittent?" to "Constant",
                "Is there redness around the joints?" to "Yes",
                "Do you have a history of arthritis?" to "No",
                "Does the pain worsen with activity?" to "Yes",
                "Do you have difficulty moving the joint?" to "Yes",
                "Do you have any other symptoms?" to "Yes"
            ) to "Reactive Arthritis",
            listOf(
                "Do you have joint pain?" to "Yes",
                "Is the pain in one or multiple joints?" to "One",
                "Is there any swelling in the joints?" to "No",
                "Do you have stiffness in the joints?" to "Yes",
                "Is the pain constant or intermittent?" to "Intermittent",
                "Is there redness around the joints?" to "No",
                "Do you have a history of arthritis?" to "Yes",
                "Does the pain worsen with activity?" to "No",
                "Do you have difficulty moving the joint?" to "Yes",
                "Do you have any other symptoms?" to "Yes"
            ) to "Scleroderma",
            listOf(
                "Do you have joint pain?" to "Yes",
                "Is the pain in one or multiple joints?" to "Multiple",
                "Is there any swelling in the joints?" to "Yes",
                "Do you have stiffness in the joints?" to "Yes",
                "Is the pain constant or intermittent?" to "Constant",
                "Is there redness around the joints?" to "Yes",
                "Do you have a history of arthritis?" to "No",
                "Does the pain worsen with activity?" to "Yes",
                "Do you have difficulty moving the joint?" to "Yes",
                "Do you have any other symptoms?" to "Yes"
            ) to "Systemic Lupus Erythematosus"
        ),
        "Feet" to listOf(
            listOf(
                "Do you have foot pain?" to "Yes",
                "Is the pain in one or both feet?" to "One",
                "Do you have swelling in the feet?" to "Yes",
                "Do you experience tingling or numbness?" to "No",
                "Is the pain sharp or dull?" to "Sharp",
                "Do you have difficulty walking?" to "Yes",
                "Do you have a history of foot injuries?" to "No",
                "Is there any redness around the pain area?" to "Yes",
                "Do you experience burning sensation?" to "No",
                "Do you have any other symptoms?" to "No"
            ) to "Plantar Fasciitis",
            listOf(
                "Do you have foot pain?" to "Yes",
                "Is the pain in one or both feet?" to "Both",
                "Do you have swelling in the feet?" to "Yes",
                "Do you experience tingling or numbness?" to "Yes",
                "Is the pain sharp or dull?" to "Dull",
                "Do you have difficulty walking?" to "Yes",
                "Do you have a history of foot injuries?" to "No",
                "Is there any redness around the pain area?" to "No",
                "Do you experience burning sensation?" to "Yes",
                "Do you have any other symptoms?" to "Yes"
            ) to "Peripheral Neuropathy",
            listOf(
                "Do you have foot pain?" to "Yes",
                "Is the pain in one or both feet?" to "One",
                "Do you have swelling in the feet?" to "No",
                "Do you experience tingling or numbness?" to "No",
                "Is the pain sharp or dull?" to "Sharp",
                "Do you have difficulty walking?" to "No",
                "Do you have a history of foot injuries?" to "Yes",
                "Is there any redness around the pain area?" to "Yes",
                "Do you experience burning sensation?" to "No",
                "Do you have any other symptoms?" to "No"
            ) to "Foot Sprain",
            listOf(
                "Do you have foot pain?" to "Yes",
                "Is the pain in one or both feet?" to "Both",
                "Do you have swelling in the feet?" to "Yes",
                "Do you experience tingling or numbness?" to "No",
                "Is the pain sharp or dull?" to "Dull",
                "Do you have difficulty walking?" to "Yes",
                "Do you have a history of foot injuries?" to "No",
                "Is there any redness around the pain area?" to "No",
                "Do you experience burning sensation?" to "No",
                "Do you have any other symptoms?" to "Yes"
            ) to "Gout",
            listOf(
                "Do you have foot pain?" to "Yes",
                "Is the pain in one or both feet?" to "One",
                "Do you have swelling in the feet?" to "No",
                "Do you experience tingling or numbness?" to "Yes",
                "Is the pain sharp or dull?" to "Sharp",
                "Do you have difficulty walking?" to "Yes",
                "Do you have a history of foot injuries?" to "No",
                "Is there any redness around the pain area?" to "No",
                "Do you experience burning sensation?" to "No",
                "Do you have any other symptoms?" to "No"
            ) to "Morton's Neuroma",
            listOf(
                "Do you have foot pain?" to "Yes",
                "Is the pain in one or both feet?" to "Both",
                "Do you have swelling in the feet?" to "No",
                "Do you experience tingling or numbness?" to "No",
                "Is the pain sharp or dull?" to "Dull",
                "Do you have difficulty walking?" to "No",
                "Do you have a history of foot injuries?" to "No",
                "Is there any redness around the pain area?" to "No",
                "Do you experience burning sensation?" to "No",
                "Do you have any other symptoms?" to "Yes"
            ) to "Flat Feet",
            listOf(
                "Do you have foot pain?" to "Yes",
                "Is the pain in one or both feet?" to "One",
                "Do you have swelling in the feet?" to "Yes",
                "Do you experience tingling or numbness?" to "No",
                "Is the pain sharp or dull?" to "Dull",
                "Do you have difficulty walking?" to "Yes",
                "Do you have a history of foot injuries?" to "Yes",
                "Is there any redness around the pain area?" to "Yes",
                "Do you experience burning sensation?" to "No",
                "Do you have any other symptoms?" to "Yes"
            ) to "Tendinitis",
            listOf(
                "Do you have foot pain?" to "Yes",
                "Is the pain in one or both feet?" to "Both",
                "Do you have swelling in the feet?" to "Yes",
                "Do you experience tingling or numbness?" to "Yes",
                "Is the pain sharp or dull?" to "Dull",
                "Do you have difficulty walking?" to "Yes",
                "Do you have a history of foot injuries?" to "No",
                "Is there any redness around the pain area?" to "Yes",
                "Do you experience burning sensation?" to "No",
                "Do you have any other symptoms?" to "Yes"
            ) to "Diabetic Foot",
            listOf(
                "Do you have foot pain?" to "Yes",
                "Is the pain in one or both feet?" to "One",
                "Do you have swelling in the feet?" to "No",
                "Do you experience tingling or numbness?" to "Yes",
                "Is the pain sharp or dull?" to "Sharp",
                "Do you have difficulty walking?" to "Yes",
                "Do you have a history of foot injuries?" to "No",
                "Is there any redness around the pain area?" to "No",
                "Do you experience burning sensation?" to "Yes",
                "Do you have any other symptoms?" to "No"
            ) to "Nerve Compression",
            listOf(
                "Do you have foot pain?" to "Yes",
                "Is the pain in one or both feet?" to "Both",
                "Do you have swelling in the feet?" to "Yes",
                "Do you experience tingling or numbness?" to "No",
                "Is the pain sharp or dull?" to "Dull",
                "Do you have difficulty walking?" to "Yes",
                "Do you have a history of foot injuries?" to "Yes",
                "Is there any redness around the pain area?" to "No",
                "Do you experience burning sensation?" to "No",
                "Do you have any other symptoms?" to "Yes"
            ) to "Arthritis"
        ),
        "Neck" to listOf(
            listOf(
                "Do you have neck pain?" to "Yes",
                "Is the pain sharp or dull?" to "Sharp",
                "Is the pain worse with movement?" to "Yes",
                "Do you experience stiffness in the neck?" to "Yes",
                "Do you have a history of neck injuries?" to "No",
                "Do you experience tingling in the arms?" to "No",
                "Is there any swelling in the neck?" to "Yes",
                "Is the pain related to posture?" to "No",
                "Do you experience headaches with neck pain?" to "Yes",
                "Do you have any other symptoms?" to "No"
            ) to "Cervical Strain",
            listOf(
                "Do you have neck pain?" to "Yes",
                "Is the pain sharp or dull?" to "Dull",
                "Is the pain worse with movement?" to "No",
                "Do you experience stiffness in the neck?" to "Yes",
                "Do you have a history of neck injuries?" to "Yes",
                "Do you experience tingling in the arms?" to "Yes",
                "Is there any swelling in the neck?" to "No",
                "Is the pain related to posture?" to "Yes",
                "Do you experience headaches with neck pain?" to "Yes",
                "Do you have any other symptoms?" to "Yes"
            ) to "Herniated Disc",
            listOf(
                "Do you have neck pain?" to "Yes",
                "Is the pain sharp or dull?" to "Sharp",
                "Is the pain worse with movement?" to "Yes",
                "Do you experience stiffness in the neck?" to "No",
                "Do you have a history of neck injuries?" to "No",
                "Do you experience tingling in the arms?" to "No",
                "Is there any swelling in the neck?" to "No",
                "Is the pain related to posture?" to "No",
                "Do you experience headaches with neck pain?" to "No",
                "Do you have any other symptoms?" to "No"
            ) to "Muscle Strain",
            listOf(
                "Do you have neck pain?" to "Yes",
                "Is the pain sharp or dull?" to "Dull",
                "Is the pain worse with movement?" to "No",
                "Do you experience stiffness in the neck?" to "Yes",
                "Do you have a history of neck injuries?" to "Yes",
                "Do you experience tingling in the arms?" to "No",
                "Is there any swelling in the neck?" to "Yes",
                "Is the pain related to posture?" to "Yes",
                "Do you experience headaches with neck pain?" to "No",
                "Do you have any other symptoms?" to "Yes"
            ) to "Spinal Stenosis",
            listOf(
                "Do you have neck pain?" to "Yes",
                "Is the pain sharp or dull?" to "Sharp",
                "Is the pain worse with movement?" to "Yes",
                "Do you experience stiffness in the neck?" to "Yes",
                "Do you have a history of neck injuries?" to "No",
                "Do you experience tingling in the arms?" to "Yes",
                "Is there any swelling in the neck?" to "No",
                "Is the pain related to posture?" to "No",
                "Do you experience headaches with neck pain?" to "Yes",
                "Do you have any other symptoms?" to "No"
            ) to "Nerve Compression",
            listOf(
                "Do you have neck pain?" to "Yes",
                "Is the pain sharp or dull?" to "Dull",
                "Is the pain worse with movement?" to "No",
                "Do you experience stiffness in the neck?" to "No",
                "Do you have a history of neck injuries?" to "Yes",
                "Do you experience tingling in the arms?" to "No",
                "Is there any swelling in the neck?" to "No",
                "Is the pain related to posture?" to "Yes",
                "Do you experience headaches with neck pain?" to "No",
                "Do you have any other symptoms?" to "Yes"
            ) to "Osteoarthritis",
            listOf(
                "Do you have neck pain?" to "Yes",
                "Is the pain sharp or dull?" to "Sharp",
                "Is the pain worse with movement?" to "Yes",
                "Do you experience stiffness in the neck?" to "Yes",
                "Do you have a history of neck injuries?" to "Yes",
                "Do you experience tingling in the arms?" to "No",
                "Is there any swelling in the neck?" to "Yes",
                "Is the pain related to posture?" to "No",
                "Do you experience headaches with neck pain?" to "Yes",
                "Do you have any other symptoms?" to "No"
            ) to "Rheumatoid Arthritis"
        ),
        "Hands" to listOf(
            listOf(
                "Do you have hand pain?" to "Yes",
                "Is the pain in one or both hands?" to "One",
                "Do you experience tingling or numbness?" to "Yes",
                "Is there any swelling in the hands?" to "No",
                "Is the pain sharp or dull?" to "Sharp",
                "Do you have difficulty gripping objects?" to "Yes",
                "Do you experience cramps in the hands?" to "No",
                "Is there any redness around the pain area?" to "Yes",
                "Do you have a history of hand injuries?" to "No",
                "Do you have any other symptoms?" to "No"
            ) to "Carpal Tunnel Syndrome",
            listOf(
                "Do you have hand pain?" to "Yes",
                "Is the pain in one or both hands?" to "Both",
                "Do you experience tingling or numbness?" to "No",
                "Is there any swelling in the hands?" to "Yes",
                "Is the pain sharp or dull?" to "Dull",
                "Do you have difficulty gripping objects?" to "No",
                "Do you experience cramps in the hands?" to "Yes",
                "Is there any redness around the pain area?" to "No",
                "Do you have a history of hand injuries?" to "Yes",
                "Do you have any other symptoms?" to "Yes"
            ) to "Rheumatoid Arthritis",
            listOf(
                "Do you have hand pain?" to "Yes",
                "Is the pain in one or both hands?" to "One",
                "Do you experience tingling or numbness?" to "Yes",
                "Is there any swelling in the hands?" to "Yes",
                "Is the pain sharp or dull?" to "Sharp",
                "Do you have difficulty gripping objects?" to "Yes",
                "Do you experience cramps in the hands?" to "No",
                "Is there any redness around the pain area?" to "Yes",
                "Do you have a history of hand injuries?" to "No",
                "Do you have any other symptoms?" to "No"
            ) to "Gout",
            listOf(
                "Do you have hand pain?" to "Yes",
                "Is the pain in one or both hands?" to "Both",
                "Do you experience tingling or numbness?" to "No",
                "Is there any swelling in the hands?" to "No",
                "Is the pain sharp or dull?" to "Dull",
                "Do you have difficulty gripping objects?" to "No",
                "Do you experience cramps in the hands?" to "Yes",
                "Is there any redness around the pain area?" to "No",
                "Do you have a history of hand injuries?" to "Yes",
                "Do you have any other symptoms?" to "Yes"
            ) to "Tendonitis",
            listOf(
                "Do you have hand pain?" to "Yes",
                "Is the pain in one or both hands?" to "One",
                "Do you experience tingling or numbness?" to "Yes",
                "Is there any swelling in the hands?" to "No",
                "Is the pain sharp or dull?" to "Sharp",
                "Do you have difficulty gripping objects?" to "Yes",
                "Do you experience cramps in the hands?" to "No",
                "Is there any redness around the pain area?" to "Yes",
                "Do you have a history of hand injuries?" to "Yes",
                "Do you have any other symptoms?" to "No"
            ) to "Osteoarthritis",
            listOf(
                "Do you have hand pain?" to "Yes",
                "Is the pain in one or both hands?" to "Both",
                "Do you experience tingling or numbness?" to "Yes",
                "Is there any swelling in the hands?" to "Yes",
                "Is the pain sharp or dull?" to "Sharp",
                "Do you have difficulty gripping objects?" to "Yes",
                "Do you experience cramps in the hands?" to "Yes",
                "Is there any redness around the pain area?" to "Yes",
                "Do you have a history of hand injuries?" to "No",
                "Do you have any other symptoms?" to "No"
            ) to "De Quervain's Tenosynovitis"
        ),
        "Stomach" to listOf(
            listOf(
                "Do you have stomach pain?" to "Yes",
                "Is the pain sharp or cramping?" to "Sharp",
                "Do you experience nausea or vomiting?" to "Yes",
                "Is the pain constant or intermittent?" to "Constant",
                "Do you have a fever?" to "No",
                "Do you have any changes in appetite?" to "No",
                "Is there bloating?" to "Yes",
                "Do you experience diarrhea or constipation?" to "No",
                "Is there any tenderness to touch?" to "Yes",
                "Do you have any other symptoms?" to "No"
            ) to "Gastritis",
            listOf(
                "Do you have stomach pain?" to "Yes",
                "Is the pain sharp or cramping?" to "Cramping",
                "Do you experience nausea or vomiting?" to "No",
                "Is the pain constant or intermittent?" to "Intermittent",
                "Do you have a fever?" to "Yes",
                "Do you have any changes in appetite?" to "Yes",
                "Is there bloating?" to "Yes",
                "Do you experience diarrhea or constipation?" to "Yes",
                "Is there any tenderness to touch?" to "No",
                "Do you have any other symptoms?" to "Yes"
            ) to "Irritable Bowel Syndrome",
            listOf(
                "Do you have stomach pain?" to "Yes",
                "Is the pain sharp or cramping?" to "Sharp",
                "Do you experience nausea or vomiting?" to "Yes",
                "Is the pain constant or intermittent?" to "Constant",
                "Do you have a fever?" to "No",
                "Do you have any changes in appetite?" to "No",
                "Is there bloating?" to "No",
                "Do you experience diarrhea or constipation?" to "No",
                "Is there any tenderness to touch?" to "Yes",
                "Do you have any other symptoms?" to "No"
            ) to "Peptic Ulcer",
            listOf(
                "Do you have stomach pain?" to "Yes",
                "Is the pain sharp or cramping?" to "Cramping",
                "Do you experience nausea or vomiting?" to "No",
                "Is the pain constant or intermittent?" to "Intermittent",
                "Do you have a fever?" to "No",
                "Do you have any changes in appetite?" to "Yes",
                "Is there bloating?" to "Yes",
                "Do you experience diarrhea or constipation?" to "Yes",
                "Is there any tenderness to touch?" to "No",
                "Do you have any other symptoms?" to "Yes"
            ) to "Constipation",
            listOf(
                "Do you have stomach pain?" to "Yes",
                "Is the pain sharp or cramping?" to "Cramping",
                "Do you experience nausea or vomiting?" to "No",
                "Is the pain constant or intermittent?" to "Constant",
                "Do you have a fever?" to "Yes",
                "Do you have any changes in appetite?" to "No",
                "Is there bloating?" to "Yes",
                "Do you experience diarrhea or constipation?" to "No",
                "Is there any tenderness to touch?" to "Yes",
                "Do you have any other symptoms?" to "No"
            ) to "Gastroenteritis"
        )
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_whats_wrong)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.shadow2)))

        // Change title of action bar
        supportActionBar?.title = "WATER SAFETY"

        // Show back button on action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        questionTextView = findViewById(R.id.questionTextView)
        optionsSpinner = findViewById(R.id.optionsSpinner)
        nextButton = findViewById(R.id.nextButton)
        resultTextView = findViewById(R.id.resultTextView)

        nextButton.setOnClickListener {
            handleNextButtonClick()
        }

        updateStage()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Handle the home button press
                onBackPressed() // Or use finish() if you want to close the activity
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun handleNextButtonClick() {
        val selectedOption = optionsSpinner.selectedItem.toString()
        if (selectedOption != "Select an option") {
            if (currentStage == 0) {
                answersSelected["When did the symptom start?"] = selectedOption
                currentStage++
                updateStage()
            } else if (currentStage == 1) {
                painLocation = selectedOption
                currentQuestions = painLocationQuestions[painLocation] ?: emptyList()
                currentStage++
                updateStage()
            } else if (currentQuestions.isNotEmpty()) {
                val questionPair = currentQuestions.getOrNull(currentStage - 2)
                if (questionPair != null) {
                    answersSelected[questionPair.first] = selectedOption
                    currentStage++
                    updateStage()
                } else {
                    handleResults()
                }
            }
        } else {
            Toast.makeText(this, "Please select an option.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateStage() {
        when (currentStage) {
            0 -> {
                questionTextView.text = stages[currentStage]
                setupSpinner(
                    listOf(
                        "Select an option",
                        "Less than 24 hours",
                        "1-3 days",
                        "More than 3 days"
                    )
                )
            }

            1 -> {
                questionTextView.text = stages[currentStage]
                setupSpinner(
                    listOf(
                        "Select an option",
                        "Head",
                        "Chest",
                        "Abdomen",
                        "Back",
                        "Legs",
                        "Arms",
                        "Joints",
                        "Feet",
                        "Neck",
                        "Hands",
                        "Stomach"
                    )
                )
            }

            in 2 until 2 + currentQuestions.size -> {
                val questionPair = currentQuestions.getOrNull(currentStage - 2)
                if (questionPair != null) {
                    questionTextView.text = questionPair.first
                    setupSpinner(questionPair.second)
                } else {
                    handleResults()
                }
            }

            else -> {
                handleResults()
            }
        }
    }

    private fun setupSpinner(options: List<String>) {
        val adapter = ArrayAdapter(this, R.layout.spinner_item, options)
        adapter.setDropDownViewResource(R.layout.spinner_item) // Apply custom layout to dropdown
        optionsSpinner.adapter = adapter
    }



    private fun handleResults() {
        // Evaluate possible conditions
        val possibleConditions = diagnosis[painLocation]?.map { (criteria, condition) ->
            val totalCriteria = criteria.size
            var matchingCriteria = 0

            // Count the number of criteria that match the selected answers
            criteria.forEach { (question, answer) ->
                if (answersSelected[question] == answer) {
                    matchingCriteria++
                }
            }

            // Calculate confidence percentage
            val confidencePercentage = if (totalCriteria > 0) {
                (matchingCriteria.toDouble() / totalCriteria * 100).toInt()
            } else {
                0
            }

            condition to confidencePercentage
        } ?: emptyList()

        // Find the best match condition
        val bestMatch = possibleConditions.maxByOrNull { it.second } ?: "Unknown Condition" to 0

        // Display the result
        val result = "Possible Condition: ${bestMatch.first} (Confidence: ${bestMatch.second}%)"
        resultTextView.text = result
        resultTextView.visibility = View.VISIBLE

        // Hide other UI elements
        questionTextView.visibility = View.GONE
        optionsSpinner.visibility = View.GONE
        nextButton.visibility = View.GONE
        nextButton.isEnabled = false

        // Show alert after a short delay
        Handler(Looper.getMainLooper()).postDelayed({
            showAlert()
        }, 1000) // 1 second delay
    }

    private fun showAlert() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_custom, null)
        val imageView: ImageView = dialogView.findViewById(R.id.dialog_image)
        val messageTextView: TextView = dialogView.findViewById(R.id.dialog_message)
        val okButton: Button = dialogView.findViewById(R.id.dialog_button)

        messageTextView.text =
            "It's still advisable to see a personal health authority for a more accurate analysis."

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        okButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
