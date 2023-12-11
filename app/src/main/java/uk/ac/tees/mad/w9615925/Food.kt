package uk.ac.tees.mad.w9615925

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Food : AppCompatActivity() {

    private var list = mutableListOf<PetFood>()
    private lateinit var petFoodAdapter: PetFoodAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinnerPetType: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)

        spinnerPetType = findViewById(R.id.spinnerPetType)
        recyclerView = findViewById(R.id.recyclerViewPetFood)
        recyclerView.layoutManager = LinearLayoutManager(this)
        petFoodAdapter = PetFoodAdapter(list)
        recyclerView.adapter = petFoodAdapter

        fetchPetFoodData()
        setupSpinner()

    }

    private fun setupSpinner() {
        val petTypes = arrayOf("Cat", "Dog")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, petTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPetType.adapter = adapter

        spinnerPetType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedPetType = parent.getItemAtPosition(position).toString()
                updateRecyclerViewForPetType(selectedPetType)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun updateRecyclerViewForPetType(petType: String) {
        val filteredList = list.filter { it.petType == petType }
        petFoodAdapter = PetFoodAdapter(filteredList)
        recyclerView.adapter = petFoodAdapter
    }

    private fun fetchPetFoodData() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("PetFoods")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataSnapshot.children.forEach { childSnapshot ->
                    val petFood = childSnapshot.getValue(PetFood::class.java)
                    petFood?.let { list.add(it) }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }


    private fun pushSampleDataToFirebase() {
        val sampleData = generateSampleData()
        val databaseReference = FirebaseDatabase.getInstance().getReference("PetFoods")

        sampleData.forEach { petFood ->
            // Create a unique key for each pet food item
            val key = databaseReference.push().key
            key?.let {
                databaseReference.child(it).setValue(petFood)
                    .addOnSuccessListener {

                    }
                    .addOnFailureListener {

                    }
            }
        }
    }


    private fun generateSampleData(): List<PetFood> {
        val catFoods = listOf(
            PetFood("Cat", "Purina Cat Chow", "Protein: 34%, Fat: 12%, Fiber: 4%"),
            PetFood("Cat", "Meow Mix Seafood Medley", "Protein: 31%, Fat: 11%, Fiber: 4%"),
            PetFood("Cat", "Blue Buffalo Indoor Health", "Protein: 32%, Fat: 15%, Fiber: 6%"),
            PetFood("Cat", "Royal Canin Hairball Care", "Protein: 34%, Fat: 15%, Fiber: 6.8%"),
            PetFood("Cat", "Hill's Science Diet Adult", "Protein: 30%, Fat: 17%, Fiber: 2%"),
            PetFood("Cat", "Iams Proactive Health", "Protein: 32%, Fat: 15%, Fiber: 3%"),
            PetFood("Cat", "Wellness CORE Natural Grain Free", "Protein: 38%, Fat: 12%, Fiber: 5%"),
            PetFood("Cat", "Rachael Ray Nutrish Natural", "Protein: 34%, Fat: 14%, Fiber: 4%"),
            PetFood("Cat", "Fancy Feast Gravy Lovers", "Protein: 35%, Fat: 2%, Fiber: 1.5%"),
            PetFood("Cat", "Natural Balance Limited Ingredient", "Protein: 30%, Fat: 12%, Fiber: 4%")
        )

        val dogFoods = listOf(
            PetFood("Dog", "Pedigree Adult Complete Nutrition", "Protein: 21%, Fat: 10%, Fiber: 4%"),
            PetFood("Dog", "Purina ONE SmartBlend", "Protein: 26%, Fat: 16%, Fiber: 3%"),
            PetFood("Dog", "Blue Buffalo Life Protection Formula", "Protein: 24%, Fat: 14%, Fiber: 5%"),
            PetFood("Dog", "Hill's Science Diet Adult Sensitive Stomach", "Protein: 20%, Fat: 13%, Fiber: 4%"),
            PetFood("Dog", "Wellness CORE Natural Grain Free", "Protein: 34%, Fat: 16%, Fiber: 4%"),
            PetFood("Dog", "Taste of the Wild High Prairie", "Protein: 32%, Fat: 18%, Fiber: 4%"),
            PetFood("Dog", "Canidae PURE Limited Ingredient", "Protein: 32%, Fat: 18%, Fiber: 4%"),
            PetFood("Dog", "Orijen Original Dry Dog Food", "Protein: 38%, Fat: 18%, Fiber: 4%"),
            PetFood("Dog", "Natural Balance L.I.D.", "Protein: 21%, Fat: 10%, Fiber: 4%"),
            PetFood("Dog", "Iams Proactive Health", "Protein: 25%, Fat: 14%, Fiber: 5%")
        )

        return catFoods + dogFoods
    }





}
data class PetFood(
    val petType: String = "",
    val name: String = "",
    val nutrition: String = ""
)
