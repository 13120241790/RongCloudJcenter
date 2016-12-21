# SealTalk Powered by RongCloud


# Use

### Step 1 with gradle 

```Java

dependencies {

    compile 'cn.rongcloud.android:IMLib:2.8.4'
    compile 'cn.rongcloud.android:IMKit:2.8.4'
    compile 'cn.rongcloud.android:CallLib:2.8.4'
    compile 'cn.rongcloud.android:CallKit:2.8.4'
    compile 'cn.rongcloud.android:RedPacket:2.8.4'
}

```

- Iterative update the version number
- IMLib is IM core
- IMKit is IM UI
- CallLib is Audio and video core
- CallKit is Audio and video UI
- CallLib or CallKit Selectable


### Step 2 init to Application.java

```Java

RongIM.init(this);

```

Config : manifest.xml

### Step 3 connect rongcloud server

```Java

RongIM.connect(token)

```

[About token](http://www.rongcloud.cn/docs/index.html)

### Step 4 Other

- Use ConversationFragment
- Use ConversationListFragment
- Friends
- Groups

...

[Document](http://www.rongcloud.cn/docs/android.html)


# More

[rongcloud web](http://rongcloud.cn/)

[sealtalk to github](https://github.com/sealtalk/sealtalk-android)


