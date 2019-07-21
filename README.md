##  **ExpandableToolbar**

show internet connectivity state in your toolbar
inspired from [Facebook Messenger](https://play.google.com/store/apps/details?id=com.facebook.orca&hl=en) app

![](https://hostpic.xyz/files/15637493701438257122.png)

## Demo

![](https://i.imgur.com/7hq4oSR.gif%5D%28https://i.imgur.com/7hq4oSR.gif)

## usage

```xml
<com.roacult.expandabletoolbar.ExpandableToolbar

		android:id="@+id/expand_toolbar"  
		android:layout_width="match_parent"  
		android:layout_height="wrap_content"
		
		app:successText="you are connected"  
		app:failText="no internet connection"  
		app:successColor="@color/success_color"  
		app:failColor="@color/fail_color"  
		app:textColor="@color/white"  
		app:animationDuration="3000"  
		app:hidingDuration = "3000"> 
```

`app:successText` : text shown when device is connected (default  "you are connected" )

`app:failText` : text shown when device is disconnected (default  "no internet connection" )

`app:successColor` : background color  when device is connected (default  #106B00 green )

`app:failColor` : background color  when device is disconnected (default  #B00020 red)

` app:animationDuration`  : duration length to expand view (in milliseconds) 

`app:hidingDuration` : duration length to hide view when device is connected (in milliseconds)


```kotlin
	val expandableView = findViewById<ExpandableToolbar>(R.id.expand_toolbar)
	
	// you can access toolbar like this  
	val toolbar = expandableView.toolbar  
	toolbar.title = getString(R.string.app_name)  
	toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"))
	
	//you can change all xml properties programmatically
	expandableView.failText = getString(R.string.failText)
	expandableView.successText= getString(R.string.successText)
	expandableView.failColor = Color.parseColor("#B00020 ")
	expandableView.animationDuration = 3000 // 3 seconds
	...

```

## Download

Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
}
```
Add the dependency
```gradle
dependencies {
	implementation 'com.github.roiacult:ExpandableToolbar:VERSION' 
}
```
Note : replace " VERSION " with latest version from jitpack ![](https://jitpack.io/v/roiacult/ExpandableToolbar.svg)
