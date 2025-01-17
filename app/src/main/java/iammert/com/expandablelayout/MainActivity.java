package iammert.com.expandablelayout;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import iammert.com.expandablelib.ExpandCollapseListener;
import iammert.com.expandablelib.ExpandableLayout;
import iammert.com.expandablelib.Section;

public class MainActivity extends AppCompatActivity {

    String[] parents = new String[]{"Fruits",
            "Nice Fruits", "Cool Fruits",
            "Perfect Fruits", "Frozen Fruits",
            "Warm Fruits"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editText = (EditText) findViewById(R.id.edittext);
        AppCompatButton buttonExpandAll = findViewById(R.id.buttonExpandAll);
        AppCompatButton buttonCollapseAll = findViewById(R.id.buttonCollapseAll);
        ExpandableLayout sectionLinearLayout = (ExpandableLayout) findViewById(R.id.el);

        // I want only one section expanded
        sectionLinearLayout.setOnlyOneSectionExpanded(true);

        buttonExpandAll.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sectionLinearLayout.expandAllSection();
            }
        });

        buttonCollapseAll.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sectionLinearLayout.collapseAllSection();
            }
        });

        sectionLinearLayout.setRenderer(new ExpandableLayout.Renderer<FruitCategory, Fruit>() {
            @Override
            public void renderParent(View view, FruitCategory model, boolean isExpanded, int parentPosition) {
                ((TextView) view.findViewById(R.id.tvParent)).setText(model.name);
                view.findViewById(R.id.arrow).setBackgroundResource(isExpanded ? R.drawable.arrow_up : R.drawable.arrow_down);
            }

            @Override
            public void renderChild(View view, Fruit model, int parentPosition, int childPosition) {
                ((TextView) view.findViewById(R.id.tvChild)).setText(model.name);
            }
        });

        sectionLinearLayout.addSection(getSection());
        sectionLinearLayout.addSection(getSection());
        sectionLinearLayout.addSection(getSection());
        sectionLinearLayout.addSection(getSection());
        sectionLinearLayout.addSection(getSection());
        sectionLinearLayout.addSection(getSection());
        sectionLinearLayout.addSection(getSection());
        sectionLinearLayout.addSection(getSection());
        sectionLinearLayout.addSection(getSection());
        sectionLinearLayout.addSection(getSection());
        sectionLinearLayout.addSection(getSection());

        sectionLinearLayout.setExpandListener((ExpandCollapseListener.ExpandListener<FruitCategory>) (parentIndex, parent, view) -> {

        });

        sectionLinearLayout.setCollapseListener((ExpandCollapseListener.CollapseListener<FruitCategory>) (parentIndex, parent, view) -> {

        });
    }

    public Section<FruitCategory, Fruit> getSection() {
        Section<FruitCategory, Fruit> section = new Section<>();
        FruitCategory fruitCategory = new FruitCategory(parents[(int) (Math.random() * parents.length)]);
        Fruit fruit1 = new Fruit("Apple");
        Fruit fruit2 = new Fruit("Orange");
        Fruit fruit3 = new Fruit("Banana");
        Fruit fruit4 = new Fruit("Grape");
        Fruit fruit5 = new Fruit("Strawberry");
        Fruit fruit6 = new Fruit("Cherry");

        section.parent = fruitCategory;
        section.children.add(fruit1);
        section.children.add(fruit2);
        section.children.add(fruit3);
        section.children.add(fruit4);
        section.children.add(fruit5);
        section.children.add(fruit6);
        section.expanded = true;
        return section;
    }
}
