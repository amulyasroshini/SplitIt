package com.example.splitit

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.splitit.databinding.ActivitySeperateBinding

class Seperate : AppCompatActivity() {

    private lateinit var binding: ActivitySeperateBinding
    private lateinit var result : TextView
    private lateinit var calculate : Button
    private lateinit var add : Button
    private lateinit var a : TextView
    private lateinit var b: TextView
    private lateinit var c : TextView
    private lateinit var cool : TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeperateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val group = intent.getStringExtra("group")
        val ppl = intent.getStringExtra("people")
        val names = intent.getStringArrayExtra("names")
        val paid = binding.editTextText
        val who = binding.editTextText3
        val why = binding.editTextText2
        result = findViewById(R.id.ress)
        calculate = findViewById(R.id.calculate)
        add = findViewById(R.id.addd)
        a = findViewById(R.id.textView)
        b = findViewById(R.id.textView2)
        c= findViewById(R.id.textView3)
        cool = findViewById(R.id.textView5)
        expenseList.clear()

        val y = intent.getStringExtra("names") ?: "" // Selects "{A,B,C,D,E,F,G,H}"

        val nameList: List<String> = y
            ?.removeSurrounding("{", "}")
            ?.split(",")
            ?.map { it.trim() }
            ?: emptyList()

        val formattedNames = nameList.joinToString(separator = ", ")
        binding.textView5.text = nameList.joinToString(", ")

        add.setOnClickListener {
            val amountStr = paid.text.toString()
            val payer = who.text.toString().trim()
            val desc = why.text.toString().trim()

            if (amountStr.isEmpty() || payer.isEmpty()) {
                Toast.makeText(this, "Please enter Amount and Payer Name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!nameList.contains(payer)) {
                Toast.makeText(this, "$payer is not in this group! ($nameList)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountStr.toDouble()
            val newExpense = Expense(amount, payer, desc)

            expenseList.add(newExpense)

            Toast.makeText(this, "Added: $desc by $payer for $amount", Toast.LENGTH_SHORT).show()

            paid.text.clear()
            who.text.clear()
            why.text.clear()


        }





        //binding.hesaru.text = names
        binding.jana.text = ppl
        binding.gname.text = group

        binding.calculate.setOnClickListener {
            if (expenseList.isEmpty()) {
                binding.ress.text = "No expenses added yet."
            } else {
                val balances = calculateNetBalances(nameList, expenseList)
                val settlements = getSettlementPlan(balances)

                binding.ress.text = settlements.joinToString("\n")
            }
        }

    }
}

fun calculateNetBalances(
    groupMembers: List<String>,
    expenses: List<Expense>
): Map<String, Double> {

    val balances = groupMembers.associateWith { 0.0 }.toMutableMap()

    for (expense in expenses) {
        val splitAmount = expense.amount / groupMembers.size

        val currentPayerBalance = balances[expense.payerName] ?: 0.0
        balances[expense.payerName] = currentPayerBalance + expense.amount

        for (member in groupMembers) {
            val currentMemberBalance = balances[member] ?: 0.0
            balances[member] = currentMemberBalance - splitAmount
        }
    }

    return balances
}

fun getSettlementPlan(balances: Map<String, Double>): List<String> {
    val instructions = mutableListOf<String>()

    val debtors = balances.filter { it.value < -0.01 }.toMutableMap() // Tolerance for floating point errors
    val creditors = balances.filter { it.value > 0.01 }.toMutableMap()

    while (debtors.isNotEmpty() && creditors.isNotEmpty()) {

        val debtor = debtors.keys.first()
        val creditor = creditors.keys.first()

        val amountOwed = Math.abs(debtors[debtor]!!)
        val amountToReceive = creditors[creditor]!!

        val payment = minOf(amountOwed, amountToReceive)

        instructions.add("$debtor pays $creditor:  ${"%.2f".format(payment)}")

        // Update remaining balances
        val remainingDebt = amountOwed - payment
        val remainingCredit = amountToReceive - payment

        // Update or remove from lists
        if (remainingDebt < 0.01) debtors.remove(debtor) else debtors[debtor] = -remainingDebt
        if (remainingCredit < 0.01) creditors.remove(creditor) else creditors[creditor] = remainingCredit
    }
    return instructions

}


val expenseList = mutableListOf<Expense>()
