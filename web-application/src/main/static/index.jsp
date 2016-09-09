<%--
  ~ Copyright 2016 Microprofile.io
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~     http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>

<!--
  ~ Copyright 2016 Microprofile.io
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~     http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  -->
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Microprofile Conference</title>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta http-equiv="x-ua-compatible" content="ie=edge">

    <link rel="stylesheet" href="assets/css/bootstrap.min.css">

    <!-- 1. Load libraries -->
    <!-- Polyfill(s) for older browsers -->
    <script src="assets/js/node_modules/core-js/client/shim.min.js"></script>
    <script src="assets/js/node_modules/zone.js/dist/zone.js"></script>
    <script src="assets/js/node_modules/reflect-metadata/Reflect.js"></script>
    <script src="assets/js/node_modules/systemjs/dist/system.src.js"></script>

    <!-- 2. Configure SystemJS -->
    <script src="assets/js/systemjs.config.js"></script>
    <script>
        System.import('app').catch(function(err){ console.error(err); });
    </script>
</head>
<!-- 3. Display the application -->
<body>
<!--[if lt IE 9]>
<p class="browsehappy">You are using an <strong>outdated</strong> browser. Please <a href="http://outdatedbrowser.com/">upgrade your browser</a> to improve your experience.</p>
<![endif]-->
<h3>conference</h3>
<microprofile-conference>Loading...</microprofile-conference>
</body>
</html>