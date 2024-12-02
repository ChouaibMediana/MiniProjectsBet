package ca.qc.cgodin.isbet

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ca.qc.cgodin.isbet.databinding.ActivityMainBinding
import java.util.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val images = arrayOf(
        R.drawable.banane, R.drawable.charbon, R.drawable.diamant, R.drawable.emeraude, R.drawable.img7,
        R.drawable.piece, R.drawable.rubis, R.drawable.sacargent, R.drawable.tresor, R.drawable.fsa
    )

    private var solde = 1000
    private var mise = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvSolde.text = getString(R.string.solde_text, solde)
        updateButtonStates()

        binding.btnJouer.setOnClickListener {
            mise = when (binding.rgMise.checkedRadioButtonId) {
                R.id.rb_mise1 -> 1
                R.id.rb_mise2 -> 2
                R.id.rb_mise5 -> 5
                else -> 0
            }

            if (solde >= mise) {
                jouer()
            } else {
                Toast.makeText(this, R.string.insufficient_balance, Toast.LENGTH_SHORT).show()
            }
        }

        binding.etCodeSecret.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val codeSecret = binding.etCodeSecret.text.toString()
                if (codeSecret == "12345") { // Vérifie si le code est correct
                    solde += 100
                    binding.tvSolde.text = getString(R.string.solde_text, solde)
                    Toast.makeText(this, R.string.secret_code_success, Toast.LENGTH_SHORT).show()
                    binding.etCodeSecret.text.clear()  // Efface le champ
                    updateButtonStates() // Actualise l'état des boutons
                }
            }
        }

        if (savedInstanceState != null) {
            solde = savedInstanceState.getInt("solde", 1000)  // Charge le solde sauvegardé
            mise = savedInstanceState.getInt("mise", 0)
            binding.cbCasseCou.isChecked = savedInstanceState.getBoolean("isCasseCou", false)
            binding.tvSolde.text = getString(R.string.solde_text, solde)
            updateButtonStates()  // Met à jour l'état des boutons en fonction du solde
        }
    }

    private fun jouer() {
        mise = when (binding.rgMise.checkedRadioButtonId) {
            R.id.rb_mise1 -> 1
            R.id.rb_mise2 -> 2
            R.id.rb_mise5 -> 5
            else -> 0 }
        if (solde < mise) {
            Toast.makeText(this, R.string.insufficient_balance, Toast.LENGTH_SHORT).show()
            updateButtonStates()
            return }
        solde -= mise
        binding.tvSolde.text = getString(R.string.solde_text, solde)
        val random = Random()
        val slot1 = images[random.nextInt(images.size)]
        val slot2 = images[random.nextInt(images.size)]
        val slot3 = images[random.nextInt(images.size)]
        binding.ivSlot1.setImageResource(slot1)
        binding.ivSlot2.setImageResource(slot2)
        binding.ivSlot3.setImageResource(slot3)
        val gain = calculateGain(slot1, slot2, slot3)
        solde += gain  // Add the gain to the balance (could be zero if no win)
        binding.tvSolde.text = getString(R.string.solde_text, solde)
        if (gain > 0) {
            Toast.makeText(this, getString(R.string.toast_win_message, gain), Toast.LENGTH_SHORT).show()
        }
        updateButtonStates()
    }


    private fun updateButtonStates() {
        binding.rbMise1.isEnabled = solde >= 1
        binding.rbMise2.isEnabled = solde >= 2
        binding.rbMise5.isEnabled = solde >= 5
        binding.btnJouer.isEnabled = solde >= mise

        // Affiche un message si le solde est insuffisant pour jouer avec la mise choisie
        if (solde < mise) {
            Toast.makeText(this, R.string.insufficient_balance, Toast.LENGTH_SHORT).show()
        }
    }

    private fun calculateGain(slot1: Int, slot2: Int, slot3: Int): Int {
        val isCasseCou = binding.cbCasseCou.isChecked
        val geraldGodinImage = R.drawable.img7
        val fsaImage = R.drawable.fsa
        val miseMultiplier: Int

        if (!isCasseCou) {
            // Normal mode
            miseMultiplier = when {
                slot1 == slot2 && slot2 == slot3 -> 25  // Three identical images
                slot1 == slot2 || slot2 == slot3 || slot1 == slot3 -> 1  // Two identical images
                else -> 0  // No match, no win
            }
        } else {
            // Casse-Cou mode
            miseMultiplier = when {
                (slot1 == geraldGodinImage && slot2 == geraldGodinImage) ||
                        (slot2 == geraldGodinImage && slot3 == geraldGodinImage) ||
                        (slot1 == geraldGodinImage && slot3 == geraldGodinImage) -> 10  // Two Gérald-Godin images
                slot1 == fsaImage && slot2 == fsaImage && slot3 == fsaImage -> 100  // Three FSA images
                else -> 0  // No match, no win
            }
        }

        return mise * miseMultiplier
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("solde", solde)  // Sauvegarde du solde actuel
        outState.putInt("mise", mise)
        outState.putBoolean("isCasseCou", binding.cbCasseCou.isChecked)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        solde = savedInstanceState.getInt("solde", 1000)  // Restaure le solde à la valeur sauvegardée
        mise = savedInstanceState.getInt("mise", 0)
        binding.cbCasseCou.isChecked = savedInstanceState.getBoolean("isCasseCou", false)
        binding.tvSolde.text = getString(R.string.solde_text, solde)
        updateButtonStates()  // Actualise les états des boutons après la restauration
    }
}
