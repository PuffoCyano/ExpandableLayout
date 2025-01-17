[![](https://jitpack.io/v/PuffoCyano/ExpandableLayout.svg)](https://jitpack.io/#PuffoCyano/ExpandableLayout)
# ExpandableLayout
Expandable LinearLayout
<img src="https://raw.githubusercontent.com/iammert/ExpandableLayout/master/art/ell.png"/>

# Setup
## Dependency
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.PuffoCyano:ExpandableLayout:v2.1'
}
```
## Layout
```xml
<iammert.com.expandablelib.ExpandableLayout
    android:id="@+id/el"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:parentLayout="@layout/layout_parent"
    app:childLayout="@layout/layout_child"/>
```
## Set renderer
```java
expandableLayout.setRenderer(new ExpandableLayout.Renderer<FruitCategory, Fruit>() {
    @Override
    public void renderParent(View view, FruitCategory model, boolean isExpanded, int parentPosition) {
        ((TextView) view.findViewById(R.id.tvParent)).setText(model.name);
    }

    @Override
    public void renderChild(View view, Fruit model, int parentPosition, int childPosition) {
        ((TextView) view.findViewById(R.id.tvChild)).setText(model.name);
    }
});
```
## Set listeners
```java
expandableLayout.setExpandListener(new ExpandCollapseListener.ExpandListener<FruitCategory>() {
    @Override
    public void onExpanded(int parentIndex, FruitCategory parent, View view) {
        //Layout expanded 
    }
});

expandableLayout.setCollapseListener(new ExpandCollapseListener.CollapseListener<FruitCategory>() {
    @Override
    public void onCollapsed(int parentIndex, FruitCategory parent, View view) {
        //Layout collapsed
    }
});
```
## Add section or children
```java
Section<FruitCategory, Fruit> section = new Section<>();

//defaut is false
section.expanded = true;

FruitCategory fruitCategory = new FruitCategory("Fruits");
Fruit fruit1 = new Fruit("Apple");
Fruit fruit2 = new Fruit("Orange");

section.parent = fruitCategory;
section.children.add(fruit1);
section.children.add(fruit2);

expandableLayout.addSection(section);
expandableLayout.addChild(fruitCategory, new Fruit("Grape"));
```

## Methods
With the new version 2.0 you can allow expanding only one section at a time:
```java
// I want only one section expanded
sectionLinearLayout.setOnlyOneSectionExpanded(true);
```
Added other methods:
- `public void collapseSection(Section section)` to collapse only one section;
- `public void collapseAllSection()` to collapse all sections;
- `public void expandSection(Section section)` to expand only one section;
- `public void expandAllSection()` to expand all sections.

License
--------


    Copyright 2019 PuffoCyano.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.






