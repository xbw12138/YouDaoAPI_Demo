# YouDaoAPI_Demo
更新
更新服务器端，展示我们搜索过的单词。
监听摇一摇服务启动桌面悬浮搜索框，避免了输入法与其他应用的冲突，也解决了有悬浮窗的时候返回键失效的问题。
使用有道API

```
{
    "translation":[
        "医生"
    ],
    "basic":{
        "us-phonetic":"'dɑktɚ",
        "phonetic":"'dɒktə",
        "uk-phonetic":"'dɒktə",
        "explains":[
            "vt. 修理；篡改，伪造；为…治病；授以博士学位",
            "n. 医生；博士",
            "vi. 就医；行医"
        ]
    },
    "query":"doctor",
    "errorCode":0,
    "web":[
        {
            "value":[
                "医生",
                "博士",
                "医生 (星际旅行)"
            ],
            "key":"Doctor"
        },
        {
            "value":[
                "굿 닥터",
                "Good Doctor (TV series)",
                "好医生"
            ],
            "key":"Good Doctor"
        },
        {
            "value":[
                "巫医",
                "鼠来宝主题曲",
                "巫"
            ],
            "key":"Witch Doctor"
        }
    ]
}
```
![image](https://github.com/xbw12138/YouDaoAPI_Demo/blob/master/ScreenShot/Screenshot_20170312-184907.png)
![image](https://github.com/xbw12138/YouDaoAPI_Demo/blob/master/ScreenShot/Screenshot_20170312-184922.png)

![image](https://github.com/xbw12138/YouDaoAPI_Demo/blob/master/ScreenShot/Screenshot_20170313-225304.png)

发现了一个比较棒的网站，可以直接根据json转化为gson java类，很方便


[](http://www.jsonschema2pojo.org/)http://www.jsonschema2pojo.org/

开源的呢

