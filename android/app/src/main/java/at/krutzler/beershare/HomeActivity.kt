package at.krutzler.beershare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        findViewById<Button>(R.id.btnExplore).setOnClickListener {
            val intent = Intent(this, BeerCellarExplorerActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnOrders).setOnClickListener {
            val intent = Intent(this, BeerOrderListActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnBeerCellars).setOnClickListener {
            val intent = Intent(this, BeerCellarListActivity::class.java)
            startActivity(intent)
        }
    }
}