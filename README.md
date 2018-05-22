# Gen

## 概要

DBの情報を元に用意したテンプレートからファイルを生成するCLIツール

- Databaseから取得したテーブル/カラム情報をjsonに書き出す
- Velocityテンプレートエンジンをベースにしたテンプレートを元にファイルを生成する

## 動作確認環境

| name | version |
| :-- | :-- |
| OS | macOS High Sierra 10.13.2 |
| Java | openjdk version "1.8.0_152" |

※ DBはPostgreSQL 9.6.2で動作確認を行なっている。

### build

`build/libs/gen-x.x.x-SNAPSHOT.jar` にRunnable JARを生成

```
$ ./gradlew build
```

## Usage

### command

#### init

デフォルトはjavaコマンド実行ディレクトリに`workspace`ディレクトリを生成し、
設定ファイルのサンプルを配備する

```
$ java -jar gen.jar init
```

outputDirectory オプションで出力先を設定できる

```
$ java -jar gen.jar init --outputDirectory=${path/to/dist}
```

#### table info

databases.jsonにて指定されたDBへ接続し、取得したテーブル/カラム情報をjsonに書き出す

```
$ java -jar gen.jar tableInfo
```

オプションを指定しない場合は`./workspace/settings.yaml`の設定を使用して処理を行う

任意のオプション、settingファイルを指定できる

```
$ java -jar gen.jar tableInfo --settings=${path/to/settings.json}
$ java -jar gen.jar tableInfo --databases=${path/to/databases.json} --outputDirectory=${path/to/dist}
```

#### generate

databases.jsonにて指定されたDBへ接続し、取得したテーブル/カラム情報をjsonに書き出す

```
$ java -jar gen.jar generate
```

オプションを指定しない場合は`./workspace/settings.yaml`の設定を使用して処理を行う

任意のオプション、settingファイルを指定できる
TODO: オプション追記

### テンプレート

Velocityを使用している。

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

### Helper

TODO: VelocityRendererの内容を記載


