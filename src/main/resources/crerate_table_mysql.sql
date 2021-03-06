-- システム名:稼働状況管理システム
-- スキーマ名:INMNUSR


use inmane; 
-- テーブル作成
DROP TABLE IF EXISTS USERS; 
CREATE TABLE USERS( 
  USER_ID varchar(10) NOT NULL comment 'ユーザID'
  , PASSWORD varchar(60) NOT NULL comment 'パスワード'
  , USER_NAME varchar(80) NOT NULL comment 'ユーザ名'
  , ROLE_NAME varchar(5) NOT NULL comment 'ロール名'
  , LOGIN_FAILURE_COUNT int(1) DEFAULT 0 NOT NULL comment 'ログイン失敗回数'
  , LOGIN_DENIED_AT timestamp comment 'ログイン拒否日時'
  , UPDATER_ID varchar(10) comment '更新者'
  , UPDATED_AT timestamp comment '更新年月日'
  , CREATER_ID varchar(10) NOT NULL comment '登録者'
  , CREATED_AT timestamp NOT NULL comment '登録年月日'
  , CONSTRAINT PK_USERS PRIMARY KEY (USER_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='ユーザマスタ';



DROP TABLE IF EXISTS STAFFS; 
CREATE TABLE STAFFS( 
  STAFF_NO varchar(5) NOT NULL comment 'スタッフNo'
  , STAFF_TYPE varchar(2) NOT NULL comment 'スタッフ種別'
  , STAFF_NAME varchar(60) NOT NULL comment '氏名'
  , STAFF_NAME_KANA varchar(120) NOT NULL comment '氏名（かな）'
  , BIRTH_DATE DATE NOT NULL comment '生年月日'
  , JOINED_DATE DATE NOT NULL comment '入社年月日'
  , UPDATER_ID varchar(10) comment '更新者'
  , UPDATED_AT timestamp comment '更新年月日'
  , CREATER_ID varchar(10) NOT NULL comment '登録者'
  , CREATED_AT timestamp NOT NULL comment '登録年月日'
  , CONSTRAINT PK_STAFFS PRIMARY KEY (STAFF_NO)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='スタッフマスタ';



DROP TABLE IF EXISTS CLIENTS; 
CREATE TABLE CLIENTS( 
  CLIENT_ID varchar(36) NOT NULL comment '取引先ID'
  , CLIENT_NAME varchar(200) NOT NULL comment '取引先名'
  , SALES_STAFF_NO varchar(10) NOT NULL comment '担当営業No'
  , UPDATER_ID varchar(10) comment '更新者'
  , UPDATED_AT timestamp comment '更新年月日'
  , CREATER_ID varchar(10) NOT NULL comment '登録者'
  , CREATED_AT timestamp NOT NULL comment '登録年月日'
  , CONSTRAINT PK_CLIENTS PRIMARY KEY (CLIENT_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='取引先マスタ';



DROP TABLE IF EXISTS BULLETIN_BOARD; 
CREATE TABLE BULLETIN_BOARD( 
  BULLETIN_BOARD_ID varchar(36) NOT NULL comment 'お知らせID'
  , CONTENTS varchar(400) NOT NULL comment '内容'
  , CREATER_ID varchar(10) NOT NULL comment '登録者'
  , CREATED_AT timestamp NOT NULL comment '登録年月日'
  , CONSTRAINT PK_BULLETIN_BOARD PRIMARY KEY (BULLETIN_BOARD_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='お知らせ';



DROP TABLE IF EXISTS PROJECTS; 
CREATE TABLE PROJECTS( 
  PROJECT_ID varchar(36) NOT NULL comment '案件ID'
  , PROJECT_NAME varchar(120) NOT NULL comment '案件名'
  , CLIENT_ID varchar(36) NOT NULL comment '取引先ID'
  , BILL_ADDRESS_ID varchar(36) NOT NULL comment '請求先ID'
  , SKILLS varchar(400) comment '主要技術'
  , OVERVIEWS varchar(800) comment '業務内容'
  , REMARKS varchar(800) comment '備考'
  , UPDATER_ID varchar(10) comment '更新者'
  , UPDATED_AT timestamp comment '更新年月日'
  , CREATER_ID varchar(10) NOT NULL comment '登録者'
  , CREATED_AT timestamp NOT NULL comment '登録年月日'
  , CONSTRAINT PK_PROJECTS PRIMARY KEY (PROJECT_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='案件マスタ';



DROP TABLE IF EXISTS CONTRACTS; 
CREATE TABLE CONTRACTS( 
  CONTRACT_ID varchar(36) NOT NULL comment '契約ID報'
  , PROJECT_ID varchar(36) NOT NULL comment '案件ID'
  , STAFF_NO varchar(5) NOT NULL comment 'スタッフNo'
  , UPDATER_ID varchar(10) comment '更新者'
  , UPDATED_AT timestamp comment '更新年月日'
  , CREATER_ID varchar(10) NOT NULL comment '登録者'
  , CREATED_AT timestamp NOT NULL comment '登録年月日'
  , CONSTRAINT PK_CONTRACTS PRIMARY KEY (CONTRACT_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='契約ヘッダ情報';



DROP TABLE IF EXISTS CONTRACTS_TRAN; 
CREATE TABLE CONTRACTS_TRAN( 
  CONTRACT_ID varchar(36) NOT NULL comment '契約ID'
  , SUB_NO varchar(3) NOT NULL comment '契約枝番'
  , CONTRACT_PERIOD_FROM DATE NOT NULL comment '契約期間From'
  , CONTRACT_PERIOD_TO DATE NOT NULL comment '契約期間To'
  , CONTRACT_TYPE int(1) NOT NULL comment '契約種別'
  , UNIT_PRICE int(7) DEFAULT 0 NOT NULL comment '単価'
  , WORKING_HOURS_MIN int(3) comment '稼働時間(下限)'
  , WORKING_HOURS_MAX int(3) comment '稼働時間(上限)'
  , OVERTIME_PREMIUM_PRICE int(7) comment '超過割増分単価'
  , SHORTAGE_DEDUCTIONS_PRICE int(7) comment '未達控除分単価'
  , REMARKS varchar(800) comment '備考'
  , UPDATER_ID varchar(10) comment '更新者'
  , UPDATED_AT timestamp comment '更新年月日'
  , CREATER_ID varchar(10) NOT NULL comment '登録者'
  , CREATED_AT timestamp NOT NULL comment '登録年月日'
  , CONSTRAINT PK_CONTRACTS_TRAN PRIMARY KEY (CONTRACT_ID, SUB_NO)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='契約トラン情報';



DROP TABLE IF EXISTS BILL_ADDRESSES; 
CREATE TABLE BILL_ADDRESSES( 
  BILL_ADDRESS_ID varchar(36) NOT NULL comment '請求先ID'
  , CLIENT_ID varchar(36) NOT NULL comment '取引先ID'
  , SPECIFIED_FORMAT_TYPE int(1) DEFAULT 0 NOT NULL comment '客先指定'
  , SITE int(3) comment 'サイト'
  , BILL_UNIT_TYPE int(1) comment '請求単位種別'
  , BILL_UNIT int(3) DEFAULT 0 comment '請求単位'
  , ZIP_CODE varchar (7) comment '郵便番号'
  , ADRESS1 varchar(100) comment '住所１'
  , ADRESS2 varchar(100) comment '住所２'
  , COMPANY_NAME1 varchar(100) comment '会社名１'
  , COMPANY_NAME2 varchar(100) comment '会社名２'
  , COMPANY_NAME3 varchar(100) comment '会社名３'
  , COMPANY_NAME4 varchar(100) comment '会社名４'
  , UPDATER_ID varchar(10) comment '更新者'
  , UPDATED_AT timestamp comment '更新年月日'
  , CREATER_ID varchar(10) NOT NULL comment '登録者'
  , CREATED_AT timestamp NOT NULL comment '登録年月日'
  , CONSTRAINT PK_BILL_ADDRESSES PRIMARY KEY (BILL_ADDRESS_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='請求先マスタ';

