# Gen

## 概要

Databaseのメタデータからいろいろ作成する
メタデータはjson形式で保持する
複数DBにアクセス可能


### command

#### init

```
$ java -jar gen.jar init ${path/to/workspace}
```
デフォルトはjavaコマンド実行ディレクトリに`workspace`ディレクトリを生成
設定ファイルのスケルトンも作成

#### meta
databases.jsonの設定を元にmetadataを取得し、json化する

```
$ java -jar gen.jar meta ${path/to/databases.json} ${path/to/dist}
```

.metaディレクトリを作成し、データベース名.meta.jsonを生成する

#### generate


- パッケージ
- ファイル名
- import: Typeによって可変 or implicitなど固定
-

metaから生成
- databaseName: UpperCamel, lowerCamel
- tableName:
- columnName:
- nullable: true or false
- defaultValue


### ディレクトリ

root
- .meta: テーブル名・カラム名・カラムの型・プライマリーキー

- task
- templates
  - defaults
  - scala
  - groovy

- database.json
- mapping.json

- dist: 成果物を格納するデフォルトディレクトリ（自動生成）

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
