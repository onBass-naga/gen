# Gen

## 概要

- Databaseから取得したテーブル/カラム情報をjsonに書き出す
- Velocityテンプレートエンジンをベースにしたテンプレートを元にファイルを生成する


### command

#### init

```
$ java -jar gen.jar init
```

デフォルトはjavaコマンド実行ディレクトリに`workspace`ディレクトリを生成
設定ファイルのスケルトンも作成

```
$ java -jar gen.jar init --outputDirectory=${path/to/dist}
```

outputDirectory オプションで出力先を設定できる


#### table info

databases.jsonにて指定されたDBへ接続し、取得したテーブル/カラム情報をjsonに書き出す

```
$ java -jar gen.jar tableInfo
```

オプションを指定しない場合は`./workspace/settings.json`の設定を使用して処理を行う

```
$ java -jar gen.jar tableInfo --settings=${path/to/settings.json}
$ java -jar gen.jar tableInfo --databases=${path/to/databases.json} --outputDirectory=${path/to/dist}
```

オプションを使用できる

#### generate

databases.jsonにて指定されたDBへ接続し、取得したテーブル/カラム情報をjsonに書き出す

```
$ java -jar gen.jar tableInfo
```

オプションを指定しない場合は`./workspace/settings.json`の設定を使用して処理を行う





### テンプレート

Velocityを使用

```
$!{option.package}

$!{option.importFixed}
$!{option.importNeeded}

case class ${_.w($table.tableName).c2UC()}(
#foreach($c in ${table.columns})
    ${c.columnName}: ${c.dataType} ${_.printIf($c.isNullable, $c.columnDefault)}${_.printIf($foreach.hasNext, ",")}
#end

) $!{option.extends}
```
