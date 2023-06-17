#  크라우드 펀딩 서비스

자신의 프로젝트를 등록하고 후원을 받을 수 있는 API 입니다.

이 프로젝트에서는 결제 서비스를 연동하지 않고 테이블 관리만 수행합니다. 

## 프로젝트 기능 및 설계

### [ 사용자 모듈 ]

- 사용자 등록 및 로그인
- 사용자 정보 관리
- jwt와 spring security 사용
- gateway와 secret key 공유

### [ 프로젝트 모듈 ]

- 프로젝트 등록
- 프로젝트 정보 입력 (제목, 설명, 목표 금액, 기간 등)
- 프로젝트 업데이트 (진행 상황, 공지)

reactive kafka
- 프로젝트 업데이트 시, consumer에서 알림처리, consumer 모듈 필요

### [ 후원 모듈 ]

- 후원 모집
- 프로젝트 무산 시 결제 취소 

프로젝트 상태 관리
- 프로젝트 테이블에 "진행 중", "완료", "무산"과 같은 상태 필드를 추가. 만료 기간이 지나고 목표 금액이 미달되면 프로젝트의 상태를 "무산"으로 변경.

후원자의 결제 정보 관리
- 후원자가 결제한 정보를 저장하는 테이블을 생성. 이 테이블에는 결제 상태 필드도 추가하여 결제 완료 또는 취소 여부를 추적.

스케줄링 작업
- 만료 기간이 지나고 프로젝트 상태가 "무산"으로 변경되면, 스케줄링 작업을 통해 해당 프로젝트의 후원자들의 결제를 취소

결제 취소 처리
결제 취소 작업은 결제 상태 필드를 '취소' 로 변경하는 것으로 한다. 

### [ 검색 및 탐색 모듈 ]


- 프로젝트 검색 Elasticsearch 검색엔진 사용 예정


## ERD
 초기 구상 테이블 - 변경예정
 
 <img src="doc/image/table model.png"/>
 
## Trouble Shooting

[go to the trouble shooting section](doc/TROUBLE_SHOOTING.md)


### Tech Stack

<div align=center> 
  <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-square&logo=SpringBoot&logoColor=white"/>
  <img src="https://img.shields.io/badge/Java-007396?style=for-the-square&logo=java&logoColor=white">
  <img src="https://img.shields.io/badge/github-181717?style=for-the-square&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-square&logo=Docker&logoColor=white"/> 
  <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-square&logo=gradle&logoColor=white">
  
![Kafka](https://img.shields.io/badge/Kafka-000000?style=for-the-square&logo=apache%20kafka&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-47A248?style=for-the-square&logo=mongodb&logoColor=white)
![WebFlux](https://img.shields.io/badge/WebFlux-FF4088?style=for-the-square&logo=spring&logoColor=white)
</div>

